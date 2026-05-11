# JWT (JSON Web Token) Nedir?

JWT, kullanıcıların kimliğini doğrulamak ve yetkilendirme işlemlerini yapmak için kullanılan **stateless (sunucu tarafında oturum tutmayan)** bir güvenlik mekanizmasıdır.  

Temel amaç, kullanıcı login olduktan sonra ona bir token vermek ve bu token ile sonraki tüm isteklerde kullanıcının tekrar login olmasına gerek kalmadan sistemde tanınmasını sağlamaktır.

JWT özellikle REST API ve Spring Security gibi yapılarda çok yaygın kullanılır.

---

# JWT’nin Temel Mantığı

JWT sistemi 3 ana aşamadan oluşur:

1. Kullanıcı login olur
2. Server kullanıcıya bir token üretir
3. Kullanıcı bu token ile tüm protected endpoint’lere erişir

Bu yapı sayesinde sunucu kullanıcı bilgisi tutmaz, sadece token üzerinden doğrulama yapar.

---

# JWT Token Yapısı

JWT token 3 parçadan oluşur:

```
Header.Payload.Signature
```

## 1. Header

Token’ın türünü ve kullanılan algoritmayı içerir.

Örnek:

```json id="jwtheader"
{
  "alg": "HS256",
  "typ": "JWT"
}
```

---

## 2. Payload

Kullanıcıya ait bilgileri içerir (claims).

Örnek:

```json id="jwtpayload"
{
  "sub": "user123",
  "role": "ADMIN",
  "exp": 1710000000
}
```

Burada:
- `sub` → kullanıcı
- `role` → yetki
- `exp` → token süresi

---

## 3. Signature

Token’ın değiştirilmediğini garanti eden imzadır.

Server secret key ile oluşturulur.

---

# JWT Nasıl Çalışır?

JWT’nin çalışma mantığı şu şekildedir:

Kullanıcı login olur → sistem şifreyi kontrol eder → doğruysa JWT token üretir → kullanıcıya gönderir.

Kullanıcı sonraki isteklerde bu token’ı gönderir:

```
Authorization: Bearer <token>
```

Server bu token’ı doğrular:
- geçerli mi?
- süresi dolmuş mu?
- imza doğru mu?

Eğer her şey doğruysa kullanıcı sisteme erişir.

---

# JWT Authentication vs Authorization

JWT sistemi hem authentication hem authorization için kullanılır.

Authentication, kullanıcının kim olduğunu doğrular. Authorization ise kullanıcının hangi işlemleri yapabileceğini belirler.

Örneğin:
- Login → Authentication
- Admin endpoint erişimi → Authorization

---

# Spring Boot’ta JWT Akışı

Spring Security içinde JWT genelde bir filter üzerinden çalışır.

İstek geldiğinde şu akış oluşur:

1. Request gelir
2. JWT Filter çalışır
3. Token alınır
4. Token doğrulanır
5. User authenticate edilir
6. Request controller’a gider

---

# JWT Filter Mantığı

JWT Filter, her request’i intercept eder ve header içindeki token’ı kontrol eder.

Eğer token yoksa veya geçersizse request direkt 401 Unauthorized döner.

Eğer token doğruysa kullanıcı SecurityContext içine set edilir ve request devam eder.

---

# JWT Secret Key

JWT üretmek ve doğrulamak için bir secret key kullanılır.

Örneğin:

```properties id="jwtsecret"
security.jwt.secret=ChangeThisSecretKeyForDemoLibraryCqrsMustBeLongEnough1234
security.jwt.expiration-in-seconds=3600
security.jwt.issuer=library-cqrs
```

- secret → imzalama anahtarı
- expiration → token süresi
- issuer → token sahibi sistem

---

# JWT Expiration (Süre)

JWT token’lar belirli bir süre geçerlidir.

Süre dolduğunda:
- token geçersiz olur
- kullanıcı tekrar login olmalıdır

Bu güvenlik açısından önemlidir.

---

# JWT Kullanım Senaryoları

JWT genelde şu durumlarda kullanılır:

- Login sistemleri
- Microservice mimarileri
- REST API güvenliği
- Mobil uygulama backend’leri
- Stateless sistemler

---

# Spring Boot JWT Örnek Kullanım

Aşağıda basit bir JWT login ve kullanım akışı verilmiştir.

---

## 1. Login Request

Kullanıcı sisteme giriş yapar:

```http id="loginreq"
POST /api/auth/login
Content-Type: application/json
```

```json id="loginbody"
{
  "username": "test",
  "password": "1234"
}
```

---

## 2. Response (Token Üretimi)

Server başarılı login sonrası token döner:

```json id="loginres"
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## 3. Protected Endpoint Çağrısı

Kullanıcı artık token ile istek atar:

```http id="protectedreq"
GET /api/books
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## 4. Spring Security JWT Filter

Backend tarafında JWT filter token’ı kontrol eder:

```java id="jwtfilter"
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {

            String token = header.substring(7);

            // token validate edilir
            boolean isValid = validateToken(token);

            if (!isValid) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean validateToken(String token) {
        // signature + expiration check
        return true;
    }
}
```

---

## 5. Security Config

JWT filter chain’e eklenir:

```java id="securityconfig"
http
    .csrf(csrf -> csrf.disable())
    .sessionManagement(session ->
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    )
    .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/login").permitAll()
            .anyRequest().authenticated()
    )
    .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
    );
```
