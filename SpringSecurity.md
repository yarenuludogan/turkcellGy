# JWT Kod Mantığı

JWT sistemi Spring’de genelde 3 ana kod parçası ile çalışır:
1. Token üretme (Login)
2. Token doğrulama (Filter)
3. Security ayarı (Config)
---

# 1. JWT Token Üretme (Login Service)

Kullanıcı login olduğunda JWT burada üretilir.

```java id="jwtgen1"
@Service
public class JwtService {

    private final String SECRET = "mySecretKey";

    // Token üretme
    public String generateToken(String username, String role) {

        return Jwts.builder()
                .setSubject(username) // kullanıcı adı
                .claim("role", role)  // ekstra bilgi (role)
                .setIssuedAt(new Date()) // oluşturulma zamanı
                .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 1 saat
                .signWith(SignatureAlgorithm.HS256, SECRET) // imzalama
                .compact();
    }
}
```

# 2. JWT Token Doğrulama (Filter)

Her request buradan geçer.

```java id="jwtfilter1"
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String SECRET = "mySecretKey";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        // 1. Token var mı?
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Token'ı ayıkla
        String token = header.substring(7);

        // 3. Token içinden username çek
        String username = extractUsername(token);

        // 4. SecurityContext'e kullanıcıyı set et
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        new ArrayList<>()
                );

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    // Token decode işlemi
    private String extractUsername(String token) {

        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
```
 Spring’e kullanıcıyı bildirir

```java
SecurityContextHolder
```

> “Artık bu request’in sahibi kim?” sorusunun cevabı buraya yazılır.

---

# 3. Security Config (JWT Aktif Etme)

```java id="security1"
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {

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
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
```

---


CSRF kapatılır
JWT stateless olduğu için session yok.
Session kapatılır
Login endpoint açık bırakılır
Diğer tüm endpointler korumalıdır

### 5. JWT Filter eklenir

```java
addFilterBefore(...)
```

Yani:

> “Her request önce JWT filter’dan geçsin”

---

# 4. Login Controller (JWT üretimi tetiklenir)

```java id="login1"
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        // fake doğrulama
        if (request.getUsername().equals("admin")) {

            return jwtService.generateToken(
                    request.getUsername(),
                    "ADMIN"
            );
        }

        return "invalid login";
    }
}
```

 Kullanıcı login olur
 Username kontrol edilir
 Token üretilir
 Client’e token döner


### Request Akışı 

Bir request geldiğinde:

```text id="flow1"
1. Controller'a gelmeden önce
2. JwtFilter çalışır
3. Token doğrulanır
4. User SecurityContext'e yazılır
5. Controller çalışır
6. Authorization kontrol edilir
```
