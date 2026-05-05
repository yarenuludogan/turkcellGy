CQRS Basics

Web katmanı sadece http ile konuşur, dto ve controllerlar var, gelen veriyi iş katmanına verir;application katmanı iş akışını yönetir, command,query, handler ve mediator içerir, domain katmanı ise dto içerir.
İstek alındı, controllera geldi controller dtoya(createBookRequest) mapledi, valid çalıştı, dto createBookCommand üretti, mediator.send(command) çağırıldı, mediator commande'e göre handlerı buldu, comman handler dan iş kuralı çalışır, repository ile yazar varmı baktı, ona göre bookId döndü, repositorye geçti, db erişimi yaptı 

Client → Controller (DTO/Validation) → Command/Query → Mediator → Handler (iş kuralı) → Repository → DB → Handler (map) → Controller → Response

Yazar oluşturma
Kitap oluşturma
Kitapları listeleme
Kitabı id ile getirme
Bunu yaparken klasik katmanlar var:

web → HTTP isteklerini alır (Controller + request DTO)
application → iş kuralları/akış (Command, Query, Handler, Mediator)
domain → veritabanı entity’leri ve repository’ler
resources → uygulama ayarları
kök dosyalar → Maven ve uygulama başlangıcı
En kritik fikir: Controller doğrudan repository çağırmıyor.
Onun yerine Mediator üzerinden “istek nesnesi” (Command veya Query) gönderiyor.
Mediator da doğru Handler’ı bulup çalıştırıyor.

CQRS = Command Query Responsibility Segregation

Command: veriyi değiştirir (create/update/delete)
Bu projede:
CreateAuthorCommand
CreateBookCommand
Query: veriyi okur, değiştirmez

GetBookByIdQuery
GetAllBooksQuery
Handler

CreateAuthorCommandHandler
CreateBookCommandHandler
GetBookByIdQueryHandler
GetAllBooksQueryHandler
İstek akışı (request lifecycle)
Örnek: POST /api/books

BookController isteği alır (CreateBookRequest)
@Valid ile doğrulama yapar (NotBlank, NotNull)
Controller bir CreateBookCommand oluşturur mediator.send(command) çağırır ,SimpleMediator, CreateBookCommandHandler’ı bulur
Handler
author var mı AuthorRepository.findById ,yoksa 404 atar ,varsa BookRepository.save ile kaydeder
dönen bookId, CreatedResponse ile HTTP cevabına dönüştürülür
Mediator altyapısı (application/mediator)
Request<TResponse>
Marker interface. Command/Query sınıfları bunu implement eder.
TResponse = bu isteğin döneceği cevap tipi.

RequestHandler<TRequest, TResponse>
Her handler’ın kontratı:

requestType() → hangi request tipini işlediğini söyler
handle(request) → asıl işi yapar
Mediator
Tek metot: send(request)
Controller sadece bunu bilir, handler detayını bilmez.

SimpleMediator
Gerçek implementasyon.

Spring tüm handler bean’lerini List<RequestHandler<?, ?>> olarak enjekte eder
Constructor’da bunları Map<Class<?>, RequestHandler<?, ?>> içine koyar
send içinde request.getClass() ile doğru handler’ı bulur
yoksa IllegalArgumentException atar

Web katmanı 
Controller’lar
AuthorController (/api/authors)

POST /api/authors
request: CreateAuthorRequest
mediator ile CreateAuthorCommand gönderir
response: CreatedResponse(id)
status: 201 CREATED
BookController (/api/books)

POST /api/books → kitap oluşturur (CreateBookCommand)
GET /api/books/{id} → tek kitap (GetBookByIdQuery)
GET /api/books → tüm kitaplar (GetAllBooksQuery)
DTO’lar (web/dto)
CreateAuthorRequest → sadece name
CreateBookRequest → title, isbn, publishedYear, authorId
CreatedResponse → sadece id
Buradaki amaç: dış API sözleşmesini (DTO) iç application modelinden ayırmak.

Application: Handler’lar (application/handler)
CreateAuthorCommandHandler
CreateAuthorCommand alır:

new Author(request.name())
authorRepository.save(...)
saved.getId() döner
@Transactional
CreateBookCommandHandler
CreateBookCommand alır:

önce author var mı kontrol (findById)
yoksa ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found")
varsa Book üretip save
yeni book id döner
@Transactional
GetBookByIdQueryHandler
GetBookByIdQuery alır:

findWithAuthorById
yoksa 404 "Book not found"
varsa BookView mapler
@Transactional(readOnly = true)
GetAllBooksQueryHandler
GetAllBooksQuery alır:

findAll()
stream ile BookView listesine mapler
@Transactional(readOnly = true)