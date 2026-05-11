### Inversion Of Control 

Bir yazılım tasarım prensibidir. Uygulama içindeki objectlerin yönetimi sağlanarak bağımlılıkların en aza indirilmesi hedeflenir.  

Burada amaç projenin framework tarafından yönetilmesi ve bizim kodumuzun bu yapıyla birlikte çalışmasını sağlamaktır.

- Modülerliği artırır
- Bağımlılıkları azaltır
- Gevşek bağlılık (loose coupling) sağlar

---

### Kullanımı

Temelde bir nesne, ihtiyaç duyduğu başka bir nesneyi **kendisi oluşturur**:

```java
// Tight Coupling
public class OrderService {

    private PaymentService paymentService = new PaymentService();

}
```

Burada `OrderService`, `PaymentService` nesnesini kendisi oluşturduğu için sıkı bağlılık (tight coupling) oluşur.

---

IoC yaklaşımında ise nesne, bağımlılıklarını **kendisi oluşturmaz**, dışarıdan alır:

```java
// IoC yaklaşımı — Loose Coupling
public class OrderService {

    private PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Burada `PaymentService` nesnesi Spring tarafından verilir.

Bu uygulama yöntemine **Dependency Injection (DI)** denir.

---

### Bean

**Bean**, Spring IoC Container tarafından oluşturulan, yapılandırılan ve yönetilen nesnedir.

Örneğin:

```java
@Service
public class BookService {

}
```

Spring bu sınıfın nesnesini oluşturur ve yönetir.

---

### Bean Scope

Bir Bean'in ne sıklıkla oluşturulacağını **Scope** belirler.

| Scope | Davranış |
|---|---|---|
| Singleton | Container'da yalnızca bir örnek oluşturulur ve her yerde aynı nesne kullanılır 
| Prototype | Her istekte yeni nesne oluşturulur |
| Request | Her HTTP isteği için yeni nesne oluşturulur 
| Session | Her HTTP oturumu için yeni nesne oluşturulur 

---
### Sık Kullanılan Exception'lar

| Exception | Açıklama |
|---|---|
| `NullPointerException` | Null referans üzerinden erişim yapılmaya çalışıldığında oluşur |
| `IllegalArgumentException` | Metoda geçersiz argüman verildiğinde fırlatılır |
| `DataIntegrityViolationException` | Database constraint ihlali olduğunda oluşur (ör. unique key çakışması) |
| `EntityNotFoundException` | JPA ile aranan entity bulunamadığında oluşur |
| `MethodArgumentNotValidException` | Validation anotasyonları (`@Valid`) başarısız olduğunda oluşur |
| `HttpRequestMethodNotSupportedException` | Yanlış HTTP methodu kullanıldığında oluşur |

---
### IllegalArgumentException

```java
public void setAge(int age) {

    if (age < 0) {
        throw new IllegalArgumentException("Yaş negatif olamaz");
    }
}
```
### Sık Kullanılan Spring / Hibernate Annotation'ları

| Annotation | Açıklama |
|---|---|
| `@SpringBootApplication` | Spring Boot uygulamasının başlangıç anotasyonudur |
| `@Controller` | MVC controller tanımlar |
| `@RestController` | REST API controller oluşturur (`@Controller + @ResponseBody`) |
| `@Service` | Service katmanını belirtir |
| `@Repository` | Database erişim katmanını belirtir |
| `@Component` | Spring tarafından yönetilecek genel bean tanımlar |
| `@Autowired` | Dependency Injection yapar |
| `@RequiredArgsConstructor` | Final alanlar için constructor oluşturur (Lombok) |
| `@Configuration` | Konfigürasyon sınıfı olduğunu belirtir |
| `@Bean` | Spring container içine bean ekler |
| `@Value` | `application.properties` değerlerini alır |
| `@RequestMapping` | Genel endpoint tanımlar |
| `@GetMapping` | GET endpoint tanımlar |
| `@PostMapping` | POST endpoint tanımlar |
| `@PutMapping` | PUT endpoint tanımlar |
| `@DeleteMapping` | DELETE endpoint tanımlar |
| `@PathVariable` | URL içindeki parametreyi alır |
| `@RequestParam` | Query parameter alır |
| `@RequestBody` | HTTP body verisini Java objesine çevirir |
| `@ResponseBody` | Return değerini JSON/XML olarak döndürür |
| `@Valid` | Validation işlemini başlatır |
| `@ExceptionHandler` | Belirli exception'ları yakalar |
| `@RestControllerAdvice` | Global exception handler oluşturur |
| `@CrossOrigin` | CORS erişimine izin verir |
| `@Entity` | Sınıfı database entity'si yapar |
| `@Table` | Tablo adını belirtir |
| `@Id` | Primary key belirtir |
| `@GeneratedValue` | ID'nin otomatik üretilmesini sağlar |
| `@Column` | Kolon özelliklerini belirler |
| `@Transient` | Database'e yazılmayacak alan belirtir |
| `@OneToOne` | One-to-One ilişki kurar |
| `@OneToMany` | One-to-Many ilişki kurar |
| `@ManyToOne` | Many-to-One ilişki kurar |
| `@ManyToMany` | Many-to-Many ilişki kurar |
| `@JoinColumn` | Foreign key kolonunu belirtir |
| `@JoinTable` | Ara ilişki tablosunu belirtir |
| `@Enumerated` | Enum değerinin DB’ye nasıl yazılacağını belirtir |
| `@CreationTimestamp` | Oluşturulma tarihini otomatik ekler |
| `@UpdateTimestamp` | Güncellenme tarihini otomatik ekler |
| `@Transactional` | İşlemleri transaction içinde çalıştırır |
| `@EnableWebSecurity` | Spring Security yapılandırmasını aktif eder |
| `@PreAuthorize` | Role/authority kontrolü yapar |
| `@JsonIgnore` | JSON çıktısında alanı gizler |
| `@Data` | Getter/setter/toString oluşturur (Lombok) |
| `@Getter` | Getter oluşturur |
| `@Setter` | Setter oluşturur |
| `@NoArgsConstructor` | Parametresiz constructor oluşturur |
| `@AllArgsConstructor` | Tüm parametreli constructor oluşturur |
| `@Builder` | Builder pattern oluşturur |

---

### Validation Annotation'ları

| Annotation | Açıklama |
|---|---|
| `@NotNull` | Alan null olamaz |
| `@NotBlank` | String boş veya whitespace olamaz |
| `@NotEmpty` | Collection/String boş olamaz |
| `@Size` | Minimum ve maksimum uzunluk belirler |
| `@Min` | Minimum değer belirler |
| `@Max` | Maksimum değer belirler |
| `@Email` | Email formatı kontrol eder |
| `@Pattern` | Regex kontrolü yapar |

---

### JWT / Security Annotation'ları

| Annotation | Açıklama |
|---|---|
| `@EnableMethodSecurity` | Method bazlı security aktif eder |
| `@AuthenticationPrincipal` | Giriş yapan kullanıcıyı alır |
| `@Secured` | Role kontrolü yapar |
| `@RolesAllowed` | Yetki kontrolü yapar |


### JPA Relationship Annotation'ları


# @OneToOne

Bir kaydın yalnızca bir başka kayıtla ilişkili olduğu durumdur.

### Örnek

Bir kullanıcının yalnızca bir kimliği olması:

```text
User -> IdentityCard
1        1
```

---

### Kullanım

```java
@Entity
public class User {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "identity_card_id")
    private IdentityCard identityCard;
}
```

---

### Açıklama

| Annotation | Görevi |
|---|---|
| `@OneToOne` | Bire bir ilişki kurar |
| `@JoinColumn` | Foreign key kolonunu belirtir |

---

### @OneToMany

Bir kaydın birden fazla kayıtla ilişkili olduğu durumdur.

## Örnek

Bir kitabın birçok yorumu olabilir:

```text
Book -> Review
1       N
```

---

### Kullanım

```java
@Entity
public class Book {

    @Id
    private Long id;

    @OneToMany(mappedBy = "book")
    private List<Review> reviews;
}
```

---

### Karşı taraf

```java
@Entity
public class Review {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
}
```

---

### mappedBy Nedir?

```java
mappedBy = "book"
```

>Bir ilişkide many-to-one olan tarafı joinColumn ile FK yaparız, diğer tabloda one-to-many olan karşılığı da mappedBy ile işaretleriz. Mapped by FK olmayan pasif tarafa yazılır.

> İlişkiyi yöneten taraf `Review` entity'sindeki `book` alanıdır.

Yani foreign key:

```text
book_id
```

`Review` tablosunda tutulur.

---

# @ManyToOne

Birden fazla kaydın tek bir kayda bağlı olduğu durumdur.

### Örnek

Birçok öğrenci aynı sınıfta olabilir:

```text
Student -> Classroom
N          1
```

---

## Kullanım

```java
@Entity
public class Student {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;
}
```

---

### Database Yapısı

### Student Table

| id | name | classroom_id |
|---|---|---|
| 1 | Ahmet | 2 |
| 2 | Mehmet | 2 |

Burada iki öğrenci aynı sınıfa bağlıdır.

---

### @ManyToMany

Birden fazla kaydın birden fazla kayıtla ilişkili olduğu durumdur.

### Örnek

Bir öğrencinin birçok dersi olabilir,
bir dersin de birçok öğrencisi olabilir:

```text
Student <-> Course
N            N
```

---

# Kullanım

Hangi tarafa koyacağın sana kalmış

```java
@Entity
public class Student {

    @Id
    private Long id;

    @ManyToMany
    @JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;
}
```
```java
@Entity
public class Course {

    @Id
    private Long id;

    @ManyToMany(mappedBy = "courses")
    private List<Student> students;
}
```
---

### Join Table

Many-to-Many ilişkide otomatik ara tablo oluşur:

### student_course

| student_id | course_id |
|---|---|
| 1 | 3 |
| 1 | 5 |
| 2 | 3 |

---

### FetchType Nedir?

İlişkili verilerin ne zaman çekileceğini belirler.

| Tür | Açıklama |
|---|---|
| `LAZY` | İhtiyaç olunca getirir |
| `EAGER` | Direkt getirir |

---

## Kullanım

```java
@OneToMany(fetch = FetchType.LAZY)
private List<Review> reviews;
```

---

### Cascade Nedir?

Ana entity üzerinde işlem yapıldığında ilişkili entity'lere de uygulanmasını sağlar.

---

## Örnek

```java
@OneToMany(
    mappedBy = "book",
    cascade = CascadeType.ALL
)
private List<Review> reviews;
```

---

## Ne Sağlar?

Eğer:

```java
bookRepository.save(book);
```

yapılırsa:

- review'ler de kaydedilir
- silinirse onlar da silinir

---

### orphanRemoval Nedir?

Listeden çıkarılan child entity'nin DB'den de silinmesini sağlar.

```java
@OneToMany(
    mappedBy = "book",
    orphanRemoval = true
)
```

---

| Annotation | Anlam |
|---|---|
| `@OneToOne` | 1 -> 1 |
| `@OneToMany` | 1 -> N |
| `@ManyToOne` | N -> 1 |
| `@ManyToMany` | N -> N |

---


### JSON Sonsuz Döngü Problemi

İki entity birbirini referans ederse:

```text
Book -> Review -> Book -> Review
```

sonsuz JSON oluşabilir.

Çözüm:

```java
@JsonIgnore
```

veya:

```java
@JsonManagedReference
@JsonBackReference
```

kullanılır.
