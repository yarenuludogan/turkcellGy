## Microservices
Monolith yapıda uygulama tek bir büyük sistem olarak geliştirilir ve deploy işlemi toplu şekilde yapılır. Yani uygulamanın küçük bir kısmında değişiklik yapılsa bile tüm sistemin yeniden deploy edilmesi gerekir. Bu yapı başlangıçta daha kolay yönetilebilir olsa da uygulama büyüdükçe bakım, ölçeklenebilirlik ve geliştirme süreçleri zorlaşabilir.

Microservice mimarisinde ise sistem küçük ve bağımsız servislerden oluşur. Her servis belirli bir business görevinden sorumludur ve loose coupling yaklaşımı sayesinde servisler arasındaki bağımlılık minimum seviyede tutulur. Böylece bir serviste yapılan değişiklik diğer servisleri doğrudan etkilemez. Ayrıca her servis bağımsız olarak deploy edilebilir ve ölçeklenebilir. Bu yapı büyük ve karmaşık sistemlerde geliştirme süreçlerini daha esnek hale getirir.

Microservice yapısında servisler birbirleriyle iletişim kurmak zorundadır. En yaygın yöntem synchronous iletişimdir. Bu yöntemde bir servis, başka bir servisin API endpoint’ine HTTP isteği göndererek API call gerçekleştirir. Ancak bu yapı servislerin birbirini beklemesine neden olur. Bunun alternatifi asynchronous iletişimdir. Asenkron yapıda servisler doğrudan birbirine istek atmak yerine araya bir message broker koyar. Bu amaçla genellikle RabbitMQ veya benzeri mesajlaşma sistemleri kullanılır. Bir servis mesajı broker’a gönderir, broker ise ilgili servise bu mesajı iletir. Böylece servisler birbirinden daha bağımsız çalışabilir ve sistem daha dayanıklı hale gelir.

Bir diğer yaklaşım ise service mesh yapısıdır. Service mesh, servisler arasındaki communication süreçlerini yönetmek için kullanılan ek bir katmandır. Güvenlik, trafik yönetimi, monitoring ve load balancing gibi işlemler bu katman tarafından gerçekleştirilir. Özellikle büyük ölçekli microservice mimarilerinde yaygın olarak kullanılır. Bu yapı çoğunlukla Kubernetes ortamlarında tercih edilir. Kubernetes container tabanlı uygulamaların yönetilmesini, otomatik ölçeklenmesini ve dağıtılmasını sağlar. Büyük microservice sistemlerinin orchestration işlemlerinde önemli rol oynar.

Microservice mimarisinin bir avantajı da teknolojik bağımsızlıktır. Her servis farklı bir programlama dili veya framework kullanılarak geliştirilebilir. Servisler kendi içinde bağımsız çalışırken API, message broker veya service mesh gibi yapılar sayesinde birbiriyle iletişim kurmaya devam eder.

CI/CD süreçlerinde ise iki farklı repository yaklaşımı bulunur. Monorepo yaklaşımında tüm servisler tek bir repository içerisinde tutulur ve her servis ayrı klasörlerde yönetilir. Bu yapı merkezi yönetim açısından avantaj sağlar. Polyrepo yaklaşımında ise her microservice için ayrı repository kullanılır. Böylece servisler tamamen bağımsız geliştirilebilir ve farklı ekipler tarafından daha rahat yönetilebilir.  

### gRPC

gRPC (Google Remote Procedure Call), Google tarafından geliştirilmiş yüksek performanslı bir iletişim framework’üdür. Temel amacı, bir istemcinin uzaktaki bir sunucu üzerindeki fonksiyonları sanki kendi yerel metoduymuş gibi çağırabilmesini sağlamaktır. Bu yapı client-server iletişimini daha hızlı, verimli ve düşük gecikmeli hale getirir. gRPC, özellikle mikroservis mimarilerinde servisler arası yüksek performanslı iletişim gerektiğinde tercih edilir.

REST (Representational State Transfer) ise API tasarlamak için kullanılan mimari bir yaklaşımdır. REST’te istemci ve sunucu HTTP protokolü üzerinden iletişim kurar ve genellikle kaynak (resource) tabanlı bir yapı kullanılır. Yani istemci bir fonksiyon çağırmaz; bunun yerine sunucudan veri ister (GET), veri ekler (POST), günceller (PUT/PATCH) veya siler (DELETE). REST daha basit, yaygın ve HTTP standartlarıyla uyumlu bir yapıdır.

### REST vs gRPC

gRPC ve REST aslında API tasarlamanın iki farklı yaklaşımıdır. gRPC’de iletişim “remote procedure call” mantığıyla çalışır; yani istemci doğrudan sunucudaki bir metodu çağırır. REST’te ise iletişim resource (kaynak) odaklıdır ve HTTP metodları üzerinden veri yönetimi yapılır. Bu nedenle gRPC daha çok fonksiyon çağrısı gibi çalışırken, REST veri odaklı bir model sunar.

Performans açısından gRPC genellikle REST’e göre daha hızlıdır. Bunun nedeni gRPC’nin HTTP/2 kullanması ve veri formatı olarak JSON yerine daha kompakt olan Protocol Buffers (protobuf) kullanmasıdır. REST ise çoğunlukla HTTP/1.1 ve JSON kullanır, bu da daha okunabilir ama daha ağır bir yapı oluşturur. Bu yüzden gRPC düşük gecikme ve yüksek performans gerektiren mikroservis sistemlerinde daha avantajlıdır.

Kullanım açısından REST daha yaygın ve insan tarafından okunabilir olduğu için web API’lerinde sık tercih edilir. gRPC ise daha çok backend-to-backend (servisler arası) iletişimde kullanılır. Özetle REST daha basit ve geniş uyumluluk sunarken, gRPC daha hızlı ve verimli bir iletişim sağlar ancak öğrenme ve implementasyon açısından biraz daha karmaşıktır.

### API Gateway

API Gateway, mikroservis mimarisinde istemci ile servisler arasındaki iletişimi merkezi hale getiren, güvenliği ve yönetimi kolaylaştıran bir giriş katmanıdır. Mikroservis mimarilerinde istemciler (client) ile backend servisler arasındaki tek giriş noktası olarak çalışan bir bileşendir. Yani kullanıcı doğrudan her bir mikroservise ayrı ayrı istek atmaz; tüm istekler önce API Gateway’e gelir, gateway bu istekleri ilgili servislere yönlendirir.

Bu yapı özellikle mikroservis sistemlerinde karmaşıklığı azaltmak için kullanılır. Çünkü bir sistemde onlarca servis olabilir ve istemcinin her bir servisin endpoint’ini bilmesi hem zor hem de yönetilemez bir hale gelir. API Gateway bu noktada “reverse proxy” gibi davranarak istekleri doğru servise route eder.

API Gateway sadece yönlendirme yapmaz; aynı zamanda birçok ek sorumluluğu da üstlenebilir. Örneğin authentication (kimlik doğrulama), authorization (yetkilendirme), rate limiting (istek sınırlandırma), logging ve monitoring gibi cross-cutting concern’leri merkezi olarak yönetebilir. Böylece her mikroservisin bu işlemleri ayrı ayrı yapmasına gerek kalmaz.


### Scaling 

Scaling (ölçekleme), bir sistemin artan iş yükünü karşılayabilmesi için kapasitesinin artırılması veya optimize edilmesi anlamına gelir. Yani bir uygulama daha fazla kullanıcı, istek veya veriyle karşılaştığında performansını koruyabilmesi için büyütülmesi sürecidir.

#### 1. Vertical Scaling (Dikey ölçekleme):
Mevcut bir sunucunun gücünü artırmaktır. Örneğin RAM, CPU veya disk kapasitesini yükseltmek gibi. Tek makine güçlendirilir ama sınırı vardır.

#### 2. Horizontal Scaling (Yatay ölçekleme):
Sisteme yeni makineler ekleyerek yükü dağıtmaktır. Örneğin bir uygulamanın tek sunucuda çalışması yerine birden fazla sunucuya bölünmesi. Mikroservis ve cloud sistemlerinde en çok kullanılan yöntem budur.

Mikroservis mimarilerinde scaling özellikle önemlidir çünkü her servis bağımsız olarak ölçeklenebilir. Örneğin sadece yoğun kullanılan bir servis çoğaltılabilirken, diğer servisler aynı kalabilir. Bu da sistemi daha verimli ve maliyet açısından daha optimize hale getirir.

### Topology

Topology, bir sistemin bileşenlerinin birbiriyle nasıl bağlandığını, nasıl iletişim kurduğunu ve veri akışının nasıl yönlendiğini tanımlayan mimari yapıdır. Mikroservislerde bu kavram sadece “hangi servis kiminle konuşuyor” değil, aynı zamanda iletişimin şekli, yönü, bağımlılık seviyesi ve veri taşıma modeli gibi detayları da kapsar.

-Servisler birbirine doğrudan mı bağlı,
-Yoksa aracı bir sistem üzerinden mi iletişim kuruyor?

#### 1. Point-to-Point Topology (Doğrudan iletişim)

Bu yapıda servisler birbirine direkt HTTP/gRPC çağrısı yapar. Örneğin Order Service, User Service’e gidip kullanıcı bilgisini ister. Bu yaklaşımın avantajı basit olmasıdır çünkü arada ekstra bir katman yoktur. Ancak servis sayısı arttıkça ciddi bir problem ortaya çıkar:

-Servisler birbirine bağımlı hale gelir
-Network çağrıları artar
-“Spaghetti communication” (karmaşık çağrı ağı) oluşur

Özellikle büyük sistemlerde bu yapı ölçeklenebilirliği zorlaştırır.

#### 2. Hub-and-Spoke (API Gateway Tabanlı Topology)

Bu yapıda tüm istemciler ve bazen servisler merkezi bir noktaya bağlanır. Bu merkez genelde Spring Cloud Gateway veya NGINX gibi bir API Gateway olur.

Burada mantık şudur:

Client → API Gateway → Microservices

Avantajları:

-Tek giriş noktası olduğu için yönetimi kolaydır
-Authentication, logging, rate limiting merkezi yapılır
-Servisler dış dünyadan izole olur

Dezavantajı:

-Gateway bir “bottleneck” olabilir
-Yanlış tasarlanırsa tek hata noktası (single point of failure) olur

#### 3. Event-Driven Topology (Olay Tabanlı Mimari)

Bu en modern mikroservis yaklaşımıdır. Servisler birbirine direkt bağlanmaz, bunun yerine “event” üretir ve tüketirler.

Order Service → “OrderCreated” event’i yayınlar
Payment Service → bu event’i dinler ve ödeme işlemini başlatır
Notification Service → aynı event ile kullanıcıya mesaj gönderir

Bu yapıda arada bir message broker bulunur. Örneğin Apache Kafka veya RabbitMQ.

Avantajları:

-Servisler gevşek bağlıdır (loose coupling)
-Sistem daha dayanıklıdır
-Yük kolay dağıtılır

Dezavantajları:

-Debug etmek zor olabilir
-Event akışını takip etmek karmaşıklaşabilir

#### 4. Service Mesh Topology

Bu yapıda servisler arasındaki iletişim doğrudan uygulama tarafından yönetilmez. Bunun yerine araya bir altyapı katmanı girer (sidecar proxy’ler).

Örneğin Kubernetes ortamında çalışan sistemlerde bu yapı sık görülür. Service mesh (örneğin Istio gibi) şunları yönetir:

-Load balancing
-Service discovery
-Security (mTLS)
-Retry / timeout / circuit breaking

Bu sayede uygulama kodu sade kalır, iletişim altyapı tarafından yönetilir.
