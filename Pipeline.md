# Pipeline Nedir

Pipeline, isteğin geçtiği işlem hattıdır. Bir istek geldiğinde direkt controller’a, ardından service katmanına gitmez. 

İstek önce:

1. Logging
2. Authentication (kimlik doğrulama)
3. Authorization (yetki kontrolü)
4. Validation
5. Exception Handling

gibi katmanlardan geçer.

---

# Logging Nedir

Logging, uygulamanın çalışma sürecinde meydana gelen olayların (komut çalıştırma, sorgu, hata, uyarı vb.) kayıt altına alınmasıdır.

---

## Logging Örneği

```java
@Service
public class ProductCommandHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductCommandHandler.class);

    public void handle(CreateProductCommand command) {

        logger.info(
                "Handling CreateProductCommand for product: {}",
                command.getName()
        );

        // iş mantığı
    }
}
```

---

```java
@Service
public class ProductQueryHandler {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductQueryHandler.class);

    public ProductDto handle(GetProductByIdQuery query) {

        logger.debug(
                "Fetching product with ID: {}",
                query.getId()
        );

        // iş mantığı

        return productDto;
    }
}
```

---

# Authentication ve Authorization

| Kavram | Açıklama |
|---|---|
| Authentication | Kullanıcının kim olduğunu doğrular |
| Authorization | Kullanıcının belirli bir işlemi yapmaya yetkisi olup olmadığını kontrol eder |

---

# Pipeline Akışı

```text
HTTP Request
    ↓
JWT Filter
    ↓
Logging Filter
    ↓
Validation
    ↓
Controller
    ↓
Service / Handler
    ↓
Database
    ↓
Response
```

---

# Pipeline Behavior Nedir

Pipeline Behavior, request handler’a ulaşmadan önce ve sonra çalışan katmandır.

Amaç:

1. Tekrar eden kodları azaltmak
2. Ortak işlemleri merkezi yönetmek
3. Tüm command/query yapılarında aynı işlemleri tekrar yazmamak

Behavior’ların çalışma sırası `@Order` anotasyonu ile belirlenir.

Küçük değer önce çalışır.

---

# Behavior Sıralama Örneği

```java
@Aspect
@Component
@Order(1)
public class AuthorizationBehavior {

    @Before("execution(* com.example.service.*.*(..))")
    public void authorize(JoinPoint joinPoint) {

        System.out.println(
                "Authorization check for: "
                        + joinPoint.getSignature()
        );
    }
}
```

---

```java
@Aspect
@Component
@Order(2)
public class LoggingBehavior {

    @Before("execution(* com.example.service.*.*(..))")
    public void log(JoinPoint joinPoint) {

        System.out.println(
                "Logging action: "
                        + joinPoint.getSignature()
        );
    }
}
```

---

```java
@Aspect
@Component
@Order(3)
public class PerformanceBehavior {

    @Around("execution(* com.example.service.*.*(..))")
    public Object measure(ProceedingJoinPoint pjp)
            throws Throwable {

        long start = System.currentTimeMillis();

        Object result = pjp.proceed();

        long end = System.currentTimeMillis();

        System.out.println(
                "Execution time: "
                        + (end - start)
                        + " ms"
        );

        return result;
    }
}
```

---

```java
@Aspect
@Component
@Order(4)
public class TransactionBehavior {

    @Around("execution(* com.example.service.*.*(..))")
    @Transactional
    public Object transactional(ProceedingJoinPoint pjp)
            throws Throwable {

        return pjp.proceed();
    }
}
```

---

# Authorization Behavior

## Amaç

Kullanıcının belirli bir işlemi yapmaya yetkisi olup olmadığını kontrol eder.

Örneğin:

- `DeleteUserCommand`
- `DeleteProductCommand`

çalıştırılmadan önce kullanıcının rolü kontrol edilir.

Eğer yetki yoksa exception fırlatılır.

---

## Authorization Behavior Örneği

```java
@Component
public class AuthorizationBehavior {

    public void authorize(
            String roleRequired,
            String userRole
    ) {

        if (!userRole.equals(roleRequired)) {

            throw new SecurityException(
                    "User not authorized for this action"
            );
        }
    }
}
```

---

# Logging Behavior

## Amaç

Komut ve sorguların çalıştırılma sürecini kaydeder.

Şunlar loglanabilir:

1. Hangi handler çalıştı
2. Hangi parametrelerle çağrıldı
3. Hata oluştu mu
4. İşlem başarılı mı

Bu sayede sistemin izlenebilirliği artar.

---

## Logging Behavior Örneği

```java
@Component
public class LoggingBehavior {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingBehavior.class);

    public void log(String action, Object data) {

        logger.info(
                "Action: {}, Data: {}",
                action,
                data
        );
    }
}
```

---

# Log Seviyeleri

## 1. TRACE

En detaylı log seviyesidir.

Adım adım akış takibi için kullanılır.

```java
private static final Logger logger =
        LoggerFactory.getLogger(OrderService.class);

public void processOrder(Order order) {

    logger.trace("Starting processOrder method");

    logger.trace("Order details: {}", order);

    // iş mantığı

    logger.trace("Finished processOrder method");
}
```

Genellikle development/debug ortamında kullanılır.

---

## 2. DEBUG

Hata ayıklama amacıyla kullanılır.

```java
public Product findProductById(Long id) {

    logger.debug(
            "Fetching product with ID: {}",
            id
    );

    Product product =
            productRepository.findById(id)
                    .orElse(null);

    logger.debug(
            "Product result: {}",
            product
    );

    return product;
}
```

---

## 3. INFO

Normal sistem olaylarını kaydeder.

```java
public void createUser(User user) {

    userRepository.save(user);

    logger.info(
            "User created successfully with ID: {}",
            user.getId()
    );
}
```

Örneğin:

- başarılı işlemler
- sistem açılışı
- sistem kapanışı

---

## 4. WARN

Beklenmedik ama sistemi durdurmayan durumları belirtir.

```java
public void updateStock(
        Long productId,
        int quantity
) {

    if (quantity < 0) {

        logger.warn(
                "Negative stock update attempted for product ID: {}",
                productId
        );
    }

    productRepository.updateStock(productId, quantity);
}
```

---

## 5. ERROR

Sistemin çalışmasını engelleyen kritik hataları belirtir.

```java
public void processPayment(Payment payment) {

    try {

        paymentGateway.charge(payment);

    } catch (PaymentException e) {

        logger.error(
                "Payment failed for order ID: {}",
                payment.getOrderId(),
                e
        );

        throw e;
    }
}
```

---

# Log Seviyesi Özeti

| Log Seviyesi | Amaç | Kullanım |
|---|---|---|
| TRACE | En detaylı akış takibi | Metod giriş/çıkış |
| DEBUG | Hata ayıklama | Parametre ve sonuç loglama |
| INFO | Normal olaylar | Başarılı işlemler |
| WARN | Uyarılar | Beklenmedik durumlar |
| ERROR | Kritik hatalar | Exception ve sistem hataları |

---

# Performance Behavior

## Amaç

Bir işlemin ne kadar sürede tamamlandığını ölçer.

Özellikle CQRS’de:

- command handler
- query handler

performansını takip etmek için kullanılır.

---

## Performance Behavior Örneği

```java
@Component
public class PerformanceBehavior {

    public <T> T measure(Supplier<T> action) {

        long start = System.currentTimeMillis();

        T result = action.get();

        long end = System.currentTimeMillis();

        System.out.println(
                "Execution time: "
                        + (end - start)
                        + " ms"
        );

        return result;
    }
}
```

---

# Transaction Behavior

## Amaç

Birden fazla database işleminin tek bir transaction içinde güvenli şekilde çalışmasını sağlar.

Eğer bir adımda hata oluşursa tüm işlem geri alınır (rollback).

Spring’te genellikle `@Transactional` anotasyonu kullanılır.

---

## Transaction Behavior Örneği

```java
@Service
public class TransactionBehavior {

    @Transactional
    public void executeTransactional(Runnable action) {

        action.run();
    }
}
```

---

# Transaction Nedir

Transaction, veritabanı sistemlerinde bir grup işlemin tek bir bütün olarak çalıştırılmasıdır.

Transaction içindeki tüm işlemler:

- tamamen başarılı olur (commit)
veya
- tamamen geri alınır (rollback)

Bu sayede veri tutarlılığı korunur.

---

# ACID Özellikleri

| Özellik | Açıklama |
|---|---|
| Atomicity | İşlemler ya tamamen yapılır ya hiç yapılmaz |
| Consistency | Veri her zaman tutarlı kalır |
| Isolation | Transaction’lar birbirinden bağımsız çalışır |
| Durability | Commit edilen veri kalıcıdır |

---

# Transaction Örneği

## Bankacılık Senaryosu

```java
@Service
public class BankService {

    @Transactional
    public void transferMoney(
            Long fromAccountId,
            Long toAccountId,
            BigDecimal amount
    ) {

        Account from =
                accountRepository.findById(fromAccountId)
                        .orElseThrow();

        from.setBalance(
                from.getBalance().subtract(amount)
        );

        accountRepository.save(from);

        Account to =
                accountRepository.findById(toAccountId)
                        .orElseThrow();

        to.setBalance(
                to.getBalance().add(amount)
        );

        accountRepository.save(to);

        // hata oluşursa rollback yapılır
    }
}
```

---

# Commit ve Rollback

| Kavram | Açıklama |
|---|---|
| Commit | Transaction başarılı olur ve değişiklikler kalıcı hale gelir |
| Rollback | Transaction başarısız olur ve tüm değişiklikler geri alınır |

---

# Isolation Level Nedir

Isolation Level, transaction’ların birbirinden ne kadar izole çalışacağını belirleyen ayardır.

Aynı anda çalışan transaction’ların birbirini nasıl etkileyeceğini kontrol eder.

---

# Isolation Problemleri

| Problem | Açıklama |
|---|---|
| Dirty Read | Commit edilmemiş veriyi okumak |
| Non-Repeatable Read | Aynı sorgunun farklı sonuç döndürmesi |
| Phantom Read | Yeni satır eklenmesi nedeniyle sonuç değişmesi |

---

# READ_UNCOMMITTED

En düşük güvenlik seviyesidir.

Başka transaction’ın commit etmediği veri okunabilir.

```java
@Transactional(
        isolation = Isolation.READ_UNCOMMITTED
)
public void checkStock(Long productId) {

}
```

---

# READ_COMMITTED

Sadece commit edilmiş veri okunabilir.

Dirty read engellenir.

```java
@Transactional(
        isolation = Isolation.READ_COMMITTED
)
public void checkStock(Long productId) {

}
```

---

# REPEATABLE_READ

Aynı transaction içinde aynı sorgu tekrar çalıştırıldığında aynı sonuç döner.

```java
@Transactional(
        isolation = Isolation.REPEATABLE_READ
)
public void checkStock(Long productId) {

}
```

---

# SERIALIZABLE

En güvenli ama en yavaş isolation seviyesidir.

Transaction’lar sırayla çalışır.

```java
@Transactional(
        isolation = Isolation.SERIALIZABLE
)
public void checkStock(Long productId) {

}
```
