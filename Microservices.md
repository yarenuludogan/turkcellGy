### Microservices
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
