**HTTP  REQUEST**

Hyper Text Transfer Protocol server ve client arası veri alışverişi yapan bir protokoldür. En basit örnek olarak tarayıcıda bir adres aratıldığında tarayıcı bir HTTP isteği gönderir, server ise bize bir data döner, biz de bu datayı işler ve ön yüzde gösteririz. Durumsuz bir protokoldür yani sunucu geçmiş etkileşimleri bilgi olarak saklamaz ve her istek bağımsız olarak işlenir.

Bu işlem birkaç adımdan oluşur:

1. İstemci, istenen kaynak ve ek parametreler gibi bilgileri içeren bir HTTP istek mesajı göndererek sunucuya bir istek başlatır.
2. Sunucu, istek mesajını alır ve kaynaklarını kullanarak yanıt mesajı oluşturmak üzere işler.
3. Sunucu, istemciye genellikle istenen kaynağı (örneğin bir web sayfası) ve ek bilgileri içeren bir yanıt mesajı gönderir.
4. İstemci, yanıt mesajını alır ve genellikle içeriği bir web tarayıcısında görüntüleyerek veya bir uygulamada göstererek işler.
5. İstemci, gerektiği kadar tekrarlayarak sunucuya ek istekler gönderebilir.

**Http Metot ve İşlevleri**

HTTP sunucuya yapılacak olan isteğin tipini belirlemek için sekiz farklı method kullanır (HTTP Methods). Bu metotlara aynı zamanda HTTP Fiilleri de denir (HTTP Verbs).

Genel Metotlar aşağıdaki gibidir.

**Get**: Sunucuda halihazırda bulunan bir kaynağa erişim için kullanılır.

**Post**: Sunucu üzerinde yeni bir kaynak oluşturmak için kullanılır. Post istekleri genellikle yeni oluşturulacak kaynağa ait bilgiyi de taşırlar.

**Put**: Sunucudaki bir kaynağı güncellemek için kullanılır. Bu istekler de genellikle üzerilerinde değiştirilmek istenen bilgiyi taşırlar.

**Patch**: Bu metot da sunucudaki bir kaynağı değiştirmek için kullanılır. Put ile arasındaki fark ise Put sunucudaki kaynağı yeni bir kaynak ile değiştirmek için kullanılır iken, Patch bu kaynağında bir kısmını değiştirmeye yarar.

**Delete**: Sunucudaki bir kaynağı silmeye yarar.

Daha az kullanılan metotlar ise aşağıdaki gibidir:

**Connect**: Sunucu ile bir bağlantı oluşturma isteği gönderir. Sunucu bağlantılarını minimum yük ile test etme olanağı sağlar.

**Head**: Sunucuya aynı Get metodu gibi ancak sadece başlığı olan (Request Header), gövdesi olmayan(Request Body) bir istek gönderir. Genellikle sunucuda bir kaynak mevcut mu veya kaynağın en son güncellenme bilgisi için kullanılır.

**Options**: Sunucunun desteklediği metotları kontrol etmek için kullanılır.

**Trace**: Bu metod ile bir sunucuya istek gönderdiğinizde, aradaki tüm vekil sunucular (Proxy, Gateway) isteğin başlığına kendi IP veya DNS biglilerini eklerler. Genellikle hata ayıklama/bakım işleri için kullanılır.

# **HTTP yanıtı nedir?**

HTTP yanıtı, bir sunucunun bir HTTP isteğine karşılık olarak istemciye geri gönderdiği mesajdır. Genellikle bir durum satırı, başlıklar ve bir mesaj gövdesinden oluşur:

```
HTTP/1.1 200 OK
Date: Sun, 28 Mar 2023 10:15:00 GMT
Content-Type: application/json
Server: Apache/2.4.39 (Unix) OpenSSL/1.1.1c PHP/7.3.6
Content-Length: 1024

{
    "name": "John Doe",
    "email": "johndoe@example.com",
    "age": 30,
    "address": {
        "street": "123 Main St",
        "city": "Anytown",
        "state": "CA",
        "zip": "12345"
    }
}
```

Durum satırı, HTTP sürümünü, isteğin sonucunu gösteren bir durum kodunu ve buna karşılık gelen bir mesajı içerir.

Başlıklar, mesaj gövdesinin içerik türü veya yanıtın gönderildiği tarih ve saat gibi yanıt hakkında ek bilgiler sağlar.

Mesaj gövdesi, HTML, JSON veya XML içeriği gibi gerçek yanıt verilerini içerir.

# **Bir HTTP İsteğinin Anatomisi**

**Method** (required) — `GET` 

Sunucuya hangi tür işlem yapılacağını söyler.

**Host** (required) —  [www.google.com](http://www.google.com/)

İsteğin gönderileceği sunucunun adresi. Domain adı veya IP olabilir.

**Path** (required) —  /search

Sunucudaki hangi kaynağa/endpoint’e erişileceğini belirtir.

**HTTP version** (required) —  HTTP/2

İletişimde kullanılan protokol sürümü. 

**Headers** (optional) — `Content-Type: application/json`

İstekle birlikte ek bilgi taşır. Örn: içerik türü, yetkilendirme bilgisi, dil ayarı.

**Query String** (optional) —  `?q=test`
URL’nin sonunda `?` ile başlayan parametreler. Genelde filtreleme veya arama için kullanılır. Örn: `?page=2&sort=asc`.

**Body** (optional) — `{“q”: “test”}`
İstekle birlikte gönderilen asıl veri. Genellikle `POST` ve `PUT` isteklerinde kullanılır. JSON, XML veya form-data olabilir.

### HTTP Request Kodları ve Örnek Senaryolar

| **Kod** | **Durum** | **Açıklama** | **Gerçek Hayat Senaryosu** |
| --- | --- | --- | --- |
| 200 | OK | İstek başarılı, veri döndü. | GET ile kullanıcı listesi alındı. |
| 201 | Created | Yeni kaynak oluşturuldu. | Yeni kullanıcı başarıyla kaydedildi. |
| 204 | No Content | İşlem başarılı ama veri yok. | Kayıt silindi, boş cevap döndü. |
| 301 | Moved Permanently | URL kalıcı olarak taşındı. | Eski ürün linki yeni linke yönlendirildi. |
| 302 | Found | Geçici yönlendirme. | Login sonrası /dashboard’a yönlendirme. |
| 400 | Bad Request | Geçersiz istek yapıldı. | POST isteğinde eksik alan var. |
| 401 | Unauthorized | Yetkilendirme gerekiyor. | JWT token eksik veya süresi dolmuş. |
| 403 | Forbidden | Erişim izni yok. | Admin sayfasına yetkisiz erişim. |
| 404 | Not Found | Kaynak bulunamadı. | ID’si olmayan kullanıcı sorgulandı. |
| 409 | Conflict | Çakışma var. | Aynı e-posta ile iki kullanıcı kaydı. |
| 429 | Too Many Requests | Çok fazla istek yapıldı. | Bot, API’ye saniyede 1000 istek attı. |
| 500 | Internal Server Error | Sunucu iç hatası. | Backend’de beklenmeyen exception fırladı. |
| 502 | Bad Gateway | Gateway (örn. Nginx) backend’e ulaşamadı. | Gunicorn yanıt vermediği için Nginx hata döndü. |
| 503 | Service Unavailable | Sunucu hizmet veremiyor. | Sunucu bakımda veya aşırı yükte. |
| 504 | Gateway Timeout | Sunucudan zamanında cevap gelmedi. | API başka bir servisten cevap alamadı. |
