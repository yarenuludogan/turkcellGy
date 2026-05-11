## Spring Genel Katmanlı Mimari Özet

HTTP isteği gelir → Controller → Service → Repository → Database → geri dönüş

- **Controller (API Katmanı)**
- **Service (İş Mantığı Katmanı)**
- **Repository (Veri Erişim Katmanı)**
- **Entity (Veritabanı Modeli)**
- **DTO (Data Transfer Object - veri taşıma modeli)**

# İstek geldi

POST /api/users
```
{
"name": "Ali",
"email": "[ali@mail.com](mailto:ali@mail.com)"
}
```
# Controller

İsteği karşılayan katman, requesti alır,DTO’ya çevirir veya DTO olarak alır, service katmanına iletir, response döner(ResponseEntity)
```
@RestController
@RequestMapping("/api/users")
public class UserController {


private final UserService userService;

public UserController(UserService userService) {
    this.userService = userService;
}

@PostMapping
public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto dto) {
    UserResponseDto response = userService.createUser(dto); //service çağırdı
    return ResponseEntity.status(HttpStatus.CREATED).body(response); //response döndü
}
```



`@RequestBody` → JSON veriyi Java objesine çevirir (DTO)
# DTO

İstek alındığında entity taşımak yerine bunu kullanırız.
```
public class UserCreateDto {
private String name;
private String email;
}

public class UserResponseDto {
private Long id;
private String name;
private String email;
}
```
# Service

İş kuralı uygulanır, validasyon yapılır, repository çağırılır, DTO-Entity dönüşümü yapılır.
```
@Service
public class UserService {


private final UserRepository userRepository;

public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
}
//parametre olarak createDto alıyor responseDto dönüyor
public UserResponseDto createUser(UserCreateDto dto) { //dto dönüşümü yapıldı

    User user = new User();
    user.setName(dto.getName());
    user.setEmail(dto.getEmail());

    User savedUser = userRepository.save(user); //repo çağırıldı, entity db'e kaydedildi.

    return new UserResponseDto(
        savedUser.getId(),
        savedUser.getName(),
        savedUser.getEmail()
    );
}
```


# Entity

@Entity
@Table(name = "users")
public class User {

```
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String name;

private String email;
```

}

# Repository 

Db işlemleri yapılan katman

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

- `save()`
- `findById()`
- `findAll()`
- `delete()`

gibi hazır methodlar sağlar.

```

// Service katmanlarını controller içine inject ediyor.
// Controller bu servisler üzerinden işlem yapacak.
@Autowired
private BookService bookService;

@Autowired
private AuthorService authorService;

@Autowired
private PublisherService publisherService;

@Autowired
private CategoryService categoryService;

// ======================================================
// YENİ KİTAP EKLEME
// ======================================================

// POST isteği karşılar.
// URL örneği:
// POST /books
@PostMapping
public ResponseEntity<BookResponse> createBook(


    // Gelen JSON verisini Java objesine çevirir.
    // @Valid validation çalıştırır.
    @Valid @RequestBody BookCreateRequest request) {

// Request içindeki authorId alınır.
// DB'de bu author var mı diye kontrol edilir.
Author author = authorService.findById(request.authorId())

    // Eğer author yoksa hata fırlatılır.
    .orElseThrow(() ->
        new RuntimeException(
            "Author not found with ID: " + request.authorId()
        )
    );

// Başlangıçta publisher null.
Publisher publisher = null;

// Request içinde publisherId geldiyse çalışır.
if (request.publisherId() != null) {

    // Publisher DB'den bulunur.
    publisher = publisherService.findById(request.publisherId())

        // Bulunamazsa hata verir.
        .orElseThrow(() ->
            new RuntimeException(
                "Publisher not found with ID: "
                + request.publisherId()
            )
        );
}

// Başlangıçta category null.
Category category = null;

// categoryId geldiyse kategori aranır.
if (request.categoryId() != null) {

    category = categoryService.findById(request.categoryId())

        // Yoksa hata fırlatılır.
        .orElseThrow(() ->
            new RuntimeException(
                "Category not found with ID: "
                + request.categoryId()
            )
        );
}

// Yeni Book entity nesnesi oluşturulur.
Book book = new Book(
        request.title(),
        request.isbn(),
        request.publishedYear(),
        request.pageCount(),
        author,
        publisher,
        category
);

// Book DB'ye kaydedilir.
Book saved = bookService.save(book);

// Kaydedilen veri response DTO'ya çevrilir.
// 201 CREATED status kodu döndürülür.
return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(BookResponse.fromEntity(saved));


}

// ======================================================
// TÜM KİTAPLARI GETİR
// ======================================================

// GET isteği karşılar.
// URL:
// GET /books
@GetMapping
public ResponseEntity<List<BookResponse>> getAllBooks() {


// DB'deki tüm kitaplar alınır.
List<Book> books = bookService.findAll();

// Entity listesi response DTO listesine çevrilir.
List<BookResponse> responses = books.stream()

        // Her Book entity'si BookResponse'a çevriliyor.
        .map(BookResponse::fromEntity)

        // Listeye dönüştürülüyor.
        .toList();

// 200 OK response döner.
return ResponseEntity.ok(responses);


}

// ======================================================
// ID'YE GÖRE KİTAP GETİR
// ======================================================

// URL örneği:
// GET /books/5
@GetMapping("/{id}")
public ResponseEntity<BookResponse> getBookById(


    // URL içindeki id alınır.
    @PathVariable Long id) {

return bookService.findById(id)

        // Kitap varsa DTO'ya çevirip döndür.
        .map(book ->
            ResponseEntity.ok(
                BookResponse.fromEntity(book)
            )
        )

        // Kitap yoksa 404 dön.
        .orElse(ResponseEntity.notFound().build());


}

// ======================================================
// BAŞLIĞA GÖRE KİTAP ARA
// ======================================================

// URL örneği:
// GET /books/search/title?title=Harry
@GetMapping("/search/title")
public ResponseEntity<List<BookResponse>> getBooksByTitle(


    // Query parameter alır.
    @RequestParam String title) {

// Başlığa göre kitaplar bulunur.
List<Book> books = bookService.findByTitle(title);

// Entity -> DTO dönüşümü yapılır.
List<BookResponse> responses = books.stream()
        .map(BookResponse::fromEntity)
        .toList();

return ResponseEntity.ok(responses);


}

// ======================================================
// ISBN'E GÖRE KİTAP GETİR
// ======================================================

// URL örneği:
// GET /books/search/isbn/12345
@GetMapping("/search/isbn/{isbn}")
public ResponseEntity<BookResponse> getBookByIsbn(


    // URL'den isbn alınır.
    @PathVariable String isbn) {

// ISBN'e göre kitap bulunur.
Book book = bookService.findByIsbn(isbn);

// Kitap varsa DTO döndür.
// Yoksa 404 dön.
return book != null
        ? ResponseEntity.ok(
            BookResponse.fromEntity(book)
        )
        : ResponseEntity.notFound().build();


}

// ======================================================
// YAZARA GÖRE KİTAPLAR
// ======================================================

// URL:
// GET /books/search/author/2
@GetMapping("/search/author/{authorId}")
public ResponseEntity<List<BookResponse>> getBooksByAuthor(


    // URL'den authorId alınır.
    @PathVariable Long authorId) {

// Yazara ait kitaplar çekilir.
List<Book> books =
        bookService.findByAuthorId(authorId);

// Entity -> DTO dönüşümü yapılır.
List<BookResponse> responses = books.stream()
        .map(BookResponse::fromEntity)
        .toList();

return ResponseEntity.ok(responses);


}

// ======================================================
// KİTAP GÜNCELLE
// ======================================================

// URL:
// PUT /books/1
@PutMapping("/{id}")
public ResponseEntity<BookResponse> updateBook(


    // URL'den id alınır.
    @PathVariable Long id,

    // Body'den yeni bilgiler alınır.
    @Valid @RequestBody BookCreateRequest request) {

// Author kontrol edilir.
Author author = authorService.findById(request.authorId())
        .orElseThrow(() ->
                new RuntimeException(
                        "Author not found with ID: "
                        + request.authorId()
                )
        );

// Publisher varsa bulunur.
Publisher publisher = null;

if (request.publisherId() != null) {
    publisher = publisherService
            .findById(request.publisherId())
            .orElse(null);
}

// Category varsa bulunur.
Category category = null;

if (request.categoryId() != null) {
    category = categoryService
            .findById(request.categoryId())
            .orElse(null);
}

// Yeni güncellenecek entity oluşturulur.
Book updatedBook = new Book(
        request.title(),
        request.isbn(),
        request.publishedYear(),
        request.pageCount(),
        author,
        publisher,
        category
);

// Güncelleme işlemi yapılır.
Book saved = bookService.update(id, updatedBook);

// Güncel veri DTO olarak döndürülür.
return ResponseEntity.ok(
        BookResponse.fromEntity(saved)
);


}

// ======================================================
// KİTAP SİL
// ======================================================

// URL:
// DELETE /books/1
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteBook(

    // URL'den id alınır.
    @PathVariable Long id) {

// Kitap silinir.
bookService.delete(id);

// 204 No Content döndürülür.
return ResponseEntity.noContent().build();
```

