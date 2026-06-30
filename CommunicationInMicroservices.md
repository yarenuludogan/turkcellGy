
#### Saga, Outbox, Inbox, Retry, DLQ ve Load Balancing

#### Örnek Proje

Doküman boyunca bir e-ticaret sistemi üzerinden ilerleyeceğiz.

Sistemde aşağıdaki servisler bulunmaktadır:

- Order Service
- Payment Service
- Inventory Service
- Shipping Service
- Notification Service

Bir müşteri sipariş verdiğinde bu servislerin tamamı birlikte çalışır.

Sipariş süreci teknik olarak oldukça karmaşıktır. Çünkü sipariş oluşturulması, ödeme alınması, stok düşülmesi, kargo oluşturulması ve müşteriye bilgilendirme yapılması farklı servisler tarafından gerçekleştirilmektedir.

Monolitik bir uygulamada bunların tamamı tek transaction içerisinde yönetilebilirken mikroservis mimarisinde her servis kendi veritabanına sahip olduğu için bu yaklaşım mümkün değildir. İşte bu noktada Saga, Outbox, Inbox, Retry ve DLQ gibi patternler devreye girer.

#### Saga Pattern

Saga Pattern'in temel amacı dağıtık transaction problemini çözmektir.

Diyelim ki müşteri bir telefon sipariş etti. Order Service siparişi veritabanına kaydetti. Daha sonra Payment Service kredi kartından tahsilat yaptı. Sonrasında Inventory Service stok düşmeye çalıştı ancak stokta ürün kalmadığı ortaya çıktı.

Bu durumda sistemin nasıl davranacağı kritik hale gelir.

Eğer herhangi bir önlem alınmazsa müşterinin kartından para çekilmiş olacak fakat sipariş teslim edilmeyecektir. Mikroservislerde en sık karşılaşılan veri tutarsızlığı problemlerinden biri budur.

Saga Pattern bu problemi compensation işlemleriyle çözer. Bir adım başarısız olduğunda daha önce başarıyla tamamlanan adımlar geri alınır.

#### Gerçek Akış

```text
Order Created
      |
      v
Payment Success
      |
      v
Inventory Failed
      |
      v
Payment Refund
      |
      v
Order Cancelled
```

Bu senaryoda Inventory Service başarısız olunca Payment Service refund işlemi gerçekleştirir ve Order Service siparişi iptal eder.

#### Saga Kullanılmazsa Ne Olur?

- Sipariş oluşur.
- Para çekilir.
- Stok düşülemez.
- Sistem tutarsız hale gelir.
- Manuel müdahale gerekir.

Özellikle bankacılık, telekom ve e-ticaret projelerinde bu durum kabul edilemez.

#### Saga Örneği

```java
@KafkaListener(topics = "payment-completed")
public void handle(PaymentCompletedEvent event) {

    inventoryService.reserve(
        event.getProductId(),
        event.getQuantity()
    );
}
```

Compensation:

```java
@KafkaListener(topics = "inventory-failed")
public void compensate(InventoryFailedEvent event) {

    paymentService.refund(
        event.getPaymentId()
    );

    orderService.cancel(
        event.getOrderId()
    );
}
```

#### Transactional Outbox Pattern

Mikroservis projelerinde çok yaygın bir hata senaryosu vardır.

Order Service siparişi kaydeder ve daha sonra Kafka'ya bir event göndermeye çalışır.

Ancak iki işlem arasında uygulama çökebilir.

```text
Order Saved ✅
Kafka Event ❌
```

Bu durumda Payment Service siparişten haberdar olmaz.

Sistemde sipariş vardır ancak diğer servisler olayı hiç öğrenemez.

Outbox Pattern tam olarak bu problemi çözmek için geliştirilmiştir.

Sipariş oluşturulduğunda Kafka'ya doğrudan mesaj göndermek yerine mesaj önce Outbox tablosuna yazılır.

Sipariş kaydı ile Outbox kaydı aynı transaction içerisinde commit edilir.

Böylece sipariş kaydedildiyse event'in de kesin olarak kaydedildiği garanti edilir.

#### Outbox Tablosu

```sql
CREATE TABLE OUTBOX(
 ID UUID,
 EVENT_TYPE VARCHAR(100),
 PAYLOAD TEXT,
 STATUS VARCHAR(20)
);
```

#### Order Service Örneği

```java
@Transactional
public void createOrder(Order order){

    orderRepository.save(order);

    outboxRepository.save(
       new OutboxEvent(
          "ORDER_CREATED",
          serialize(order)
       )
    );
}
```

#### Publisher Servisi

```java
@Scheduled(fixedDelay = 3000)
public void publishEvents(){

    List<OutboxEvent> events =
        outboxRepository.findPending();

    for(OutboxEvent event : events){

       kafkaTemplate.send(
           "order-events",
           event.getPayload()
       );

       event.markProcessed();
    }
}
```

Büyük ölçekli sistemlerde Debezium kullanılarak Outbox tablosundaki değişiklikler CDC (Change Data Capture) ile Kafka'ya aktarılabilir.

#### Idempotency / Inbox Pattern

Dağıtık sistemlerde en tehlikeli problemlerden biri duplicate işlemlerdir.

Kafka varsayılan olarak birçok senaryoda mesajı en az bir kez teslim etmeyi garanti eder. Bu nedenle aynı event iki veya daha fazla kez tüketilebilir.

Örneğin PaymentCompleted event'i iki kez işlendiğinde aşağıdaki sorunlar oluşabilir:

- Aynı stok iki kez düşebilir.
- Aynı faturadan iki tane üretilebilir.
- Aynı müşteriye iki SMS gönderilebilir.
- Aynı bonus iki kez yüklenebilir.

Bu problemleri önlemek için Inbox Pattern kullanılır.

Consumer event'i işlemeye başlamadan önce event id'sinin daha önce işlenip işlenmediğini kontrol eder.

#### Inbox Akışı

```text
Event Geldi
     |
     v
Inbox Kontrolü
     |
 +---+---+
 |       |
Var     Yok
 |       |
Skip   Process
```

#### Kod Örneği

```java
@Transactional
@KafkaListener(topics = "payment-events")
public void consume(PaymentCompletedEvent event){

   if(inboxRepository.existsById(
      event.getEventId())){
      return;
   }

   inventoryService.reserve();

   inboxRepository.save(
      new InboxRecord(event.getEventId())
   );
}
```

#### Retry Mekanizması

Gerçek üretim ortamlarında servisler her zaman erişilebilir olmayabilir.

Örneğin Payment Service yoğun trafik nedeniyle 5 saniyeliğine yanıt veremiyor olabilir.

Bu durum kalıcı bir hata değildir.

Sistemin hemen vazgeçmesi yerine birkaç kez tekrar deneme yapması gerekir.

İşte Retry mekanizması bu amaçla kullanılır.

#### Yanlış Kullanım

```text
Payment Service Timeout
      |
      v
Direkt Hata
```

#### Doğru Kullanım

```text
Timeout
  |
Retry-1
  |
Retry-2
  |
Retry-3
  |
Success
```

#### Spring Retry

```java
@Retryable(
 maxAttempts = 4,
 backoff = @Backoff(
     delay = 1000,
     multiplier = 2
 )
)
public void processPayment(){

   paymentClient.call();
}
```

Bekleme süreleri:

```text
1 sn
2 sn
4 sn
8 sn
```

Bu yaklaşım servisin toparlanması için zaman tanır ve sistemin gereksiz yere başarısız olmasını engeller.

#### Dead Letter Queue (DLQ)

Bazı mesajlar ne kadar retry yapılırsa yapılsın işlenemez.

Örneğin event payload'ı eksik olabilir.

```json
{
  "customerId": null
}
```

Bu kayıt düzeltilemez bir business hatası içeriyorsa sonsuza kadar retry yapmak anlamsızdır.

Bu nedenle belirli sayıda retry sonrasında mesaj DLQ kuyruğuna gönderilir.

#### Akış

```text
Kafka Topic
     |
     v
Consumer
     |
   Hata
     |
 Retry
     |
 Retry Limit
     |
     v
DLQ
```

#### Kod Örneği

```java
@DltHandler
public void processDlt(String payload){

    log.error(
      "DLQ Message : {}",
      payload
    );
}
```

Gerçek projelerde operasyon ekipleri DLQ'yu izler.

DLQ'ya düşen kayıtlar genellikle dashboardlardan takip edilir ve sorun çözüldükten sonra tekrar publish edilir.

#### Load Balancing

Bir mikroservis başarılı olsa bile yüksek trafik altında tek başına yeterli performansı sağlayamayabilir.

Diyelim ki Order Service dakikada 30.000 istek almaya başladı.

Tek instance kullanılırsa CPU ve memory tüketimi hızla artacaktır.

Bu nedenle aynı servisin birden fazla kopyası ayağa kaldırılır.

```text
Order Service Instance-1
Order Service Instance-2
Order Service Instance-3
```

Ancak istemciler hangi instance'a gideceklerini bilemez.

Bu noktada Load Balancer devreye girer.

#### Mimari

```text
Client
   |
API Gateway
   |
Load Balancer
 /    |    S1    S2   S3
```

#### Kubernetes Örneği

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: order
spec:
  replicas: 3
```

Bu yapı sayesinde trafik 3 pod arasında dağıtılır.

Bir pod çökse bile diğer podlar servise devam eder.

#### Patternlerin Birlikte Kullanımı

Gerçek bir sipariş senaryosunda tüm patternler birlikte çalışır.

1. Order Service siparişi oluşturur.
2. Outbox tablosuna event kaydedilir.
3. Publisher event'i Kafka'ya gönderir.
4. Payment Service event'i tüketir.
5. Inbox duplicate kontrolü yapar.
6. Payment servisine erişilemezse retry devreye girer.
7. Retry limiti aşılırsa mesaj DLQ'ya düşer.
8. Sürecin herhangi bir yerinde hata oluşursa Saga compensation başlatır.
9. Tüm servisler load balancer arkasında yatay olarak ölçeklenir.

Bu nedenle modern event-driven mikroservis mimarilerinde bu patternler bağımsız değil, birbirini tamamlayan yapı taşları olarak kullanılır. Büyük ölçekli sistemlerde genellikle Saga + Outbox + Inbox + Retry + DLQ kombinasyonu birlikte uygulanır.
#### Kafka ve Mikroservislerde Kullanımı

Apache Kafka, yüksek hacimli veri akışlarını yönetmek için geliştirilmiş dağıtık bir event streaming platformudur. Mikroservis mimarilerinde servislerin birbirleriyle doğrudan haberleşmesi yerine event tabanlı haberleşme kurmak için en yaygın kullanılan teknolojilerden biridir.

Kafka kullanılmadan oluşturulan sistemlerde servisler genellikle REST çağrıları ile birbirlerine bağımlı hale gelirler.

Örneğin:

```text
Order Service
     |
     v
Payment Service
     |
     v
Inventory Service
     |
     v
Notification Service
```

Bu mimaride Payment Service çalışmazsa sipariş süreci durur. Inventory Service erişilemezse tüm akış başarısız olur. Servisler birbirine sıkı bağlı hale gelir.

Kafka kullanıldığında ise servisler event üzerinden haberleşir.

```text
Order Service
      |
      v
OrderCreated Event
      |
      v
Kafka Topic
   /        v        v
Payment   Notification
Service    Service
```

Bu yaklaşım sayesinde servisler birbirlerinden bağımsız çalışabilir.

#### Kafka'nın Temel Bileşenleri

##### Producer

Mesaj üreten uygulamadır.

Örneğin Order Service sipariş oluşturduğunda Kafka'ya event gönderir.

```java
kafkaTemplate.send(
    "order-events",
    orderCreatedEvent
);
```

##### Topic

Mesajların tutulduğu mantıksal yapılardır.

Örnek:

```text
order-events
payment-events
inventory-events
notification-events
```

##### Consumer

Topic içerisindeki mesajları okuyan servistir.

```java
@KafkaListener(topics = "order-events")
public void consume(OrderCreatedEvent event){

   processOrder(event);
}
```

##### Broker

Kafka sunucularıdır.

Production ortamlarında genellikle birden fazla broker çalıştırılır.

```text
Broker-1
Broker-2
Broker-3
```

Bu yapı yüksek erişilebilirlik sağlar.

#### Kafka Partition Mantığı

Bir topic bir veya daha fazla partition içerir.

```text
order-events

Partition-0
Partition-1
Partition-2
```

Partition'lar ölçeklenebilirliğin temelidir.

Mesajlar partition'lara dağıtılır ve aynı partition içindeki sıralama korunur.

Örneğin aynı OrderId'nin aynı partition'a gitmesi sağlanabilir.

```java
kafkaTemplate.send(
    "order-events",
    orderId,
    event
);
```

Burada orderId key olarak kullanılır.

#### Consumer Group

Birden fazla consumer aynı topic'i okuyabilir.

```text
Topic
  |
  v
Consumer Group
 |    |    |
 C1   C2   C3
```

Kafka bir partition'ı aynı consumer group içerisinde yalnızca bir consumer'a verir.

Bu sayede aynı mesaj birden fazla consumer tarafından işlenmez.

Örneğin:

```text
Partition-0 -> Consumer-1
Partition-1 -> Consumer-2
Partition-2 -> Consumer-3
```

#### Sipariş Projesinde Kafka Kullanımı

Sipariş oluşturulduğunda aşağıdaki akış çalışabilir.

```text
Order Service
      |
      v
OrderCreated Event
      |
      v
Kafka
      |
 +----+---------+
 |              |
 v              v
Payment     Notification
Service       Service
```

Payment Service ödeme işlemini başlatırken Notification Service müşteriye bilgilendirme mesajı gönderebilir.

Order Service bu servislerin varlığından haberdar olmak zorunda değildir.

#### Kafka ve Outbox Pattern

Gerçek projelerde Kafka çoğu zaman Outbox Pattern ile birlikte kullanılır.

Akış:

```text
Order Saved
     |
     v
Outbox Record Saved
     |
     v
Publisher
     |
     v
Kafka
```

Bu yapı sayesinde veri kaybı önlenir.

#### Kafka ve Inbox Pattern

Kafka genellikle At-Least-Once Delivery mantığıyla çalışır.

Bir mesaj bazı durumlarda tekrar gelebilir.

Bu nedenle consumer tarafında Inbox Pattern uygulanır.

```text
Kafka Event
     |
     v
Inbox Check
     |
     v
Business Processing
```

#### Kafka ve Saga Pattern

Saga süreçlerinde servisler genellikle eventlerini Kafka üzerinden yayınlar.

Örnek:

```text
OrderCreated
      |
      v
PaymentCompleted
      |
      v
InventoryReserved
      |
      v
ShippingCreated
```

Eğer InventoryReserved adımı başarısız olursa:

```text
InventoryFailed
       |
       v
PaymentRefunded
       |
       v
OrderCancelled
```

Bu eventlerin tamamı Kafka üzerinden taşınabilir.

#### Kafka + Saga + Outbox + Inbox + Retry + DLQ Birlikte Nasıl Çalışır?

Gerçek bir mikroservis projesinde tipik akış aşağıdaki gibidir:

1. Order Service siparişi oluşturur.
2. Order ve Outbox kaydı aynı transaction içinde veritabanına yazılır.
3. Publisher Outbox kaydını Kafka'ya gönderir.
4. Payment Service event'i tüketir.
5. Inbox duplicate kontrolü yapar.
6. Payment Service erişilemiyorsa Retry çalışır.
7. Retry limiti aşılırsa event DLQ'ya gönderilir.
8. Sürecin herhangi bir adımı başarısız olursa Saga compensation eventleri yayınlanır.
9. Tüm servisler Kubernetes üzerinde birden fazla replica olarak çalışır ve Load Balancer arkasında ölçeklenir.

Bu nedenle Kafka çoğu modern event-driven mikroservis mimarisinin merkezinde yer alır ve Saga, Outbox, Inbox, Retry ve DLQ gibi patternlerin uygulanmasını kolaylaştırır.
