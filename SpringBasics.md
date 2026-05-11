# Spring Katman Mimarisi

Bu proje için katmanlar şu sırayla çalışır:

1. Entity
2. Repository
3. Service
4. DTO
5. Controller

## 1. Entity Katmanı
- `entity` klasöründe yer alır.
- Database tablosunu Java sınıfı olarak temsil eder.
- JPA anotasyonlarıyla sütunlar ve ilişkiler tanımlanır.
- Örnek: `Category` sınıfı.
- Burada sadece veri modeli vardır, iş mantığı yoktur.

## 2. Repository Katmanı
- `repository` klasöründe yer alır.
- Database CRUD işlemlerini sağlar.
- `JpaRepository<Entity, ID>` genişletmesiyle hazır metodlar kullanılır.
- Örnek: `CategoryRepository`.
- Repository sadece entity nesneleriyle çalışır ve database erişimi döner.
//service request dto alır response dto döner
## 3. Service Katmanı
- `service` klasöründe yer alır.
- İş mantığını içerir ve veri dönüşümlerini yapar.
- Controller'dan gelen Request DTO'yu alır.
- Request DTO → Entity dönüşümü yapar.
- Repository çağrısı yapar: `save`, `findAll`, `findById`, `delete`.
- Repository'den dönen Entity'yi Response DTO'ya çevirir.
- Örnek: `CategoryServiceImpl`.
- Service, genellikle Response DTO döner; Entity değil.

### Service ne return etmeli?
- `create()` için `CreatedCategoryResponse`
- `getAll()` için `List<ListCategoryResponse>`
- `getById()` için `GetCategoryResponse`
- `update()` için `UpdatedCategoryResponse`
- `delete()` için `void`

## 4. DTO Katmanı
- `dto` klasöründe yer alır.
- HTTP istek ve cevaplarında kullanılan veri nesneleridir.
- Entity'den farklı olarak sadece kullanıcıya göndermek veya kullanıcıdan almak istediğiniz alanları içerir.
- Örnekler:
  - `CreateCategoryRequest`
  - `CreatedCategoryResponse`
  - `GetCategoryResponse`
  - `ListCategoryResponse`
  - `UpdateCategoryRequest`
  - `UpdatedCategoryResponse`
- DTO kullanımı:
  - Güvenlik için gereksiz alanları gizler.
  - Response içinde ilişkileri kontrol eder.
  - Controller ve Service arasındaki bağı zayıflatır.
  -Request Dto kullanıcının sisteme gönderdiği veriyi temsil eder.
  -Response Dto kullanıcıya API'dan dönen responsu temsil eder.
  
 ## Nasıl Kullanılır
 
  // Controller 
  @PostMapping
public BookResponse addBook(@RequestBody CreateBookRequest request) {
    return bookService.add(request);
}

## 5. Controller Katmanı
- `controller` klasöründe yer alır.
- HTTP endpoint'lerini tanımlar.
- Request DTO alır, Service metodunu çağırır, Response DTO döner.
- Örnek: `CategoriesController`.
- Controller içindeki metodlar genelde şöyle olur:
  - `@PostMapping` → create
  - `@GetMapping` → getAll
  - `@GetMapping("/{id}")` → getById
  - `@PutMapping("/{id}")` → update
  - `@DeleteMapping("/{id}")` → delete

## Veri Akışı
1. Kullanıcı HTTP isteği gönderir.
2. Controller Request DTO alır.
3. Controller Service'i çağırır.
4. Service Request DTO'yu Entity'ye çevirir.
5. Service Repository'ye Entity gönderir.
6. Repository DB işlemini yapar.
7. DB'den dönen Entity Service tarafından Response DTO'ya çevrilir.
8. Controller Response DTO'yu JSON olarak döner.

## Özet
- `Repository` sadece DB ve Entity işleri yapar.
- `Service` iş mantığını ve DTO dönüşümlerini yapar.
- `Controller` HTTP isteği alır ve HTTP cevabı döner.
- `DTO` kullanıcıya giden / kullanıcıdan gelen veriyi kontrol eder.
- `Entity` veritabanı tablosunu temsil eder.

