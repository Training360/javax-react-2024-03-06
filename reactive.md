class: inverse, center, middle

# Reaktív programozás Spring keretrendszerrel

---

class: inverse, center, middle

# Reaktív Kiáltvány

---

## Reaktív Kiáltvány 

* Változó igényeknek való megfelelés
  * Elvárás az ezredmásodperces válaszidő, 100%-os rendelkezésre állás, petabyte méretű adatok kezelése, cloud futtatókörnyezet
  * Alkalmazások hibatűrőbbek, öngyógyítóak, flexibilisebbek
  * Reactive Manifesto szerint ezeket az igényeket a reaktív rendszerek képesek kielégíteni, a következő tulajdonságokkal
* [The Reactive Manifesto](https://www.reactivemanifesto.org/)
  * Reszponzivitás (Responsive): az alkalmazásnak minden esetben gyors választ kell adnia
  * Ellenállóképesség (Resilient): az alkalmazásnak reszponzívaknak kell maradnia hiba esetén is
  * Elaszticitás (Elastic): reszponzivitás nagy terhelés esetén is
  * Üzenetvezéreltség (Message-driven): rendszerek elemei aszinkron, nem blokkoló módon, üzenetekkel kommunikálnak

---

## Mit nem várhatunk?

* Klasszikus CRUD alkalmazásoknál, normál terhelés mellett nem tapasztalunk teljesítményjavulást
* Nagy terhelés és/vagy hibák megjelenése esetén mutatkozik meg az előnye

---

class: inverse, center, middle

# Reaktív rendszerek akadályozó tényezői Javaban

---

## Akadályozó tényezők

* A reaktív rendszerek fejlesztésének számos akadályozó tényezője van Javaban

---

## Akadályozó tényezők: <br /> Szinkron IO

* CPU arra vár, hogy beérkezzen az adat (pl. fájlrendszer, teljes http request, adatbázis result set)
* A megoldás létezik Java 1.4 óta: Java NIO (New IO), másnéven Non-blocking IO
* Operációs rendszer lehetőségeit használja ki (Linux, Windows)
* Nem várja meg az olvasás eredményét, hanem egy callback, mely visszahívásra kerül, ha előállt az adat
* CPU-t szabadít fel
* `java.nio.Buffer`, pl. `ByteBuffer` - olyan pufferterület, mely konkrétan az operációs rendszerhez kötött,
  így pl. fájl olvasáskor nem kell az operációs rendszer memóriájából a JVM memóriájába átmásolni, így CPU-t takarít meg
* `java.nio.channels.Channel`, pl. `AsynchronousFileChannel` - `Buffer` írás és olvasás, tipikusan fájl és socket felé vagy felől

---

## AsynchronousFileChannel

```java
public abstract Future<Integer> read​(ByteBuffer dst,
                                     long position)

public abstract <A> void read​(ByteBuffer dst,
                              long position,
                              A attachment,
                              CompletionHandler<Integer,? super A> handler)
```

Az `Attachment` a context tárolására való, ez köthető az aktuális olvasáshoz,
ebben lehet információt átadni a `CompletionHandler` példánynak 

---

## NIO státusza

* Különösen hatékony, ha pl. fájlt kell kiszolgálni http-n, hiszen nem kell a JVM memóriába beolvasni
* Nem használjuk, túl alacsony szintű
* Kevés eszköz támogatja
* Bizonyos eszközök támogatják, pl. a Netty (NIO client server framework, hálózati alkalmazások fejlesztésére)
  * Szakít a klasszikus szinkron Servlet API hagyományokkal

---

## Akadályozó tényezők: <br /> Szálkezelés

* IO-ra várás blokkolja a szálakat
* Skálázás
  * Futtatás több szálon
  * Horizontális skálázás
* Problémák
  * 1 MB stack/thread (1000 szál esetén?)
  * Context switch, hiszen nincs annyi magos processzorunk

---

## Akadályozó tényezők: <br /> Kollekciók

* Magas absztrakciós szinten gondolkozunk, tipikusan entitások kollekciói
* Kollekciók esetén be kell gyűjteni az összes elemet
* Az `Iterator` és a `Stream` már jó előrelépés, azonban a _pull_-központúak

---

## Akadályozó tényezők: <br /> Push túlterhelés

* Amennyiben a termelő a saját ütemében állítja elő az adatot,
túlterhelheti a fogyasztót
* Ez hálózati protokolloknál ismert jelenség, megoldása a flow control, vagy push back
* Több mechanizmus is van (lásd [Wikipedia](https://en.wikipedia.org/wiki/Flow_control_(data))

---

## Akadályozó tényezők: <br /> Keretrendszer támogatás

* Hiánya
* Pl. Spring, Hibernate

---

class: inverse, center, middle

# Reaktív programozás

---

## Reaktív programozás

* Programozási paradigma, implementációs technika
* A rendszer az adatelemek folyamára reagál
* Aszinkron, nem blokkoló végrehajtás
* Back pressure: reaktív nevezéktanban mechanizmus arra, hogy a termelő ne árassza el a fogyasztót
* Non-blocking back pressure: fogyasztó kéri a következő x elemet, amit fel tud dolgozni
* Nem kivétel alapú hibakezelés, nem akasztja meg az adatfolyamot, tipikusan egy callback
* Tipikus felhasználási területei
  * Külső rendszerek hívása
  * Jelentős mennyiségű üzenet párhuzamos feldolgozása
  * Táblázatkezelés: egy cellát módosítva változik több cella tartalma

---

## Funkcionális <br /> reaktív programozás

* Funkcionális stílusban kivitelezett reaktív programozás
* Alapja a mellékhatás és állapot nélküli függvények
* Deklaratív
* Sok boilerplate kód eliminálása
* Könnyebb karbantarthatóság, jobb kódminőség
* Defacto standard megoldások
* Callback-hell ellen
* Újrafelhasználható operátorok

---

## Kitekintés: felhasználói <br /> felület (mobil) fejlesztés

* Android környezetben az RxJava elterjedt
* Szálkezelés megvalósításának egyszerűsítésére
    * Szálkezelés: felhasználói interakció, hálózat, szenzorok (pl. GPS), stb.
* Callback hell ellenszere
* Legjobb gyakorlatok (pl. a felhasználó közbülső interakcióját nem kell figyelembe venni 
	- karakterenkénti szűrés, keresés - Throttling)

---

## Reaktív megvalósítások <br /> Javaban

* Eclipse Vert.x
* Akka ($)
* RxJava
* Project Reactor

---

## Reaktív megoldások <br /> együttműködésére

* [Reactive Streams specifikáció](https://www.reactive-streams.org/)
* Observer design pattern
* `Observer` interfész Java 9 óta deprecated
* Non-blocking back pressure, rejtett, ha magas szintű API-kat használunk

---

## Java 9 Flow API

<img src="images/java-9-flow-api.png" width="600" alt="Java 9 Flow API UML class diagram" />

---

class: inverse, center, middle

# Project Reactor

---

## Project Reactor

* Reactive Streamsre épülő reactive library
* Spring közeli
* Teljes ecosystem, nem kell magunk implementálni:
  * Reactor Netty: interprocess communication, HTTP, TCP, UDP kliens/szerver, Netty-re építve
  * Reactor Kafka: Kafka integráció
  * Reactor RabbitMQ: RabbitMQ integráció

---

## pom.xml

```xml
<dependency>
  <groupId>io.projectreactor</groupId>
  <artifactId>reactor-core</artifactId>
  <version>3.5.1</version>
</dependency>
```

---

## Típusos adatfolyamok

* `Mono<T>`: nulla vagy egy elem
* `Flux<T>`: n elem
* Implementálják a `Publisher` interfészt

---

## Flux létrehozása és <br /> feliratkozás

```java
Flux.just(new Employee("John Doe", 1970),
      new Employee("Jack Doe", 1980),
      new Employee("Jane Doe", 1990))
    .subscribe(System.out::println);
```

---

## Block

```java
Employee employee = Mono.just(new Employee("John Doe", 1970))
    .block();

Employee employee = Flux.just(new Employee("John Doe", 1970),
    new Employee("Jack Doe", 1980),
    new Employee("Jane Doe", 1990))
  .blockFirst(); // .blockLast()    

List<Employee> employees = Flux.just(new Employee("John Doe", 1970),
      new Employee("Jack Doe", 1980),
      new Employee("Jane Doe", 1990))
    .collectList().block();
```

---

## Operátorok

<img src="images/flux.svg" width="600" />

[API dokumentáció](https://projectreactor.io/docs/core/release/api/)

---

## Marble diagram

<img src="images/mapForFlux.svg" width="600" />

---

## Közbülső operátorok

```java
Flux.just(
      new Employee("John Doe", 1970),
      new Employee("Jack Doe", 1980),
      new Employee("Jane Doe", 1990))
    .filter(e -> e.getYearOfBirth() <= 1980)
    .map(Employee::getName)
    .map(String::toUpperCase)
    .sort()
    .skip(1)
    .subscribe(System.out::println);
```

---

## Ugyanez Java 8 <br /> Stream API-val

```java
List.of( new Employee("John Doe", 1970),
      new Employee("Jack Doe", 1980),
      new Employee("Jane Doe", 1990))
    .stream()
    .filter(e -> e.getYearOfBirth() <= 1980)
    .map(Employee::getName)
    .map(String::toUpperCase)
    .sorted()
    .skip(1)
    .forEach(System.out::println);
```

---

## Mono és Flux közötti <br /> konvertálás

```java
Mono<Employee> employee = Flux.just(new Employee("John Doe", 1970))
  .single();

Flux<Employee> employees = Mono.just(new Employee("John Doe", 1970))
    .flux();
```

---

class: inverse, center, middle

# Hibakezelés

---

## Hiba

```java
Mono.just(new Employee("John Doe", 1970))
                .map(e -> e.getAgeAt(1960))
                .subscribe(System.out::println);
```

.small-code-14[
```
[ERROR] (main) Operator called default onErrorDropped - reactor.core.Exceptions$ErrorCallbackNotImplemented: 
  java.lang.IllegalArgumentException: Birth after year 1960
reactor.core.Exceptions$ErrorCallbackNotImplemented: java.lang.IllegalArgumentException: Birth after year 1960
Caused by: java.lang.IllegalArgumentException: Birth after year 1960
	at training.empapp.Employee.getAgeAt(Employee.java:17)
```
]

---

## Hibakezelés

```java
Mono.just(new Employee("John Doe", 1970))
    .map(e -> e.getAgeAt(1960))
    .doOnError(System.out::println)
    .onErrorResume(t -> Mono.just(-1))
    .subscribe(System.out::println);
```

```java
Mono.just(new Employee("John Doe", 1970))
    .map(e -> e.getAgeAt(1960))
    .onErrorReturn(-1)
    .subscribe(System.out::println);
```

---

class: inverse, center, middle

# Tesztelés

---

## pom.xml

```xml
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-test</artifactId>
    <version>3.5.1</version>
</dependency>
```

---

## Tesztelése

```java
Flux<String> employees = Flux.just(
      new Employee("John Doe", 1970),
      new Employee("Jack Doe", 1980),
      new Employee("Jane Doe", 1990))
    .filter(e -> e.getYearOfBirth() <= 1980)
    .map(Employee::getName)
    .map(String::toUpperCase)
    .sort()
    .skip(1);

StepVerifier.create(employees)
        .expectNext("JOHN DOE")
        .verifyComplete();
```

* `expectNextCount()`

```java
StepVerifier.create(employees)
                .expectNextMatches(name -> name.toLowerCase().contains("john"))
                .verifyComplete();
```

---

class: inverse, center, middle

# Keretrendszerek integrálása

---

## pom.xml

```xml
<dependency>
  <groupId>io.reactivex.rxjava3</groupId>
  <artifactId>rxjava</artifactId>
  <version>3.1.5</version>
</dependency>
```

---

## RxJava megvalósítás

```java
var employees = List.of(
        new Employee("John Doe", 1970),
        new Employee("Jack Doe", 1980),
        new Employee("Jane Doe", 1990)
        );

Flowable.fromIterable(employees)
    .filter(employee -> employee.getYearOfBirth() <= 1980)
    .map(Employee::getName)
    .sort()
    .skip(1);
```

---

## Keretrendszerek <br /> integrálása

```java
var employees = List.of(
        new Employee("John Doe", 1970),
        new Employee("Jack Doe", 1980),
        new Employee("Jane Doe", 1990)
        );

Flux.from(Flowable.fromIterable(employees)
        .filter(employee -> employee.getYearOfBirth() <= 1980)
        .map(Employee::getName))
        .sort()
    	.skip(1);
```

---

class: inverse, center, middle

# Webes fejlesztés

---

## Klasszikus Servlet API

<img src="images/ThreadPool.png" width="600" />

---

## Spring WebFlux

* Spring MVC alternatíva
* Spring MVC tapasztalataira építve
* Hasonló megközelítés, egymás mellett élő, független implementációk
* Reactive HTTP API-ra építve (Servlet API helyett)
* Default web konténer: Netty
* Router functions
* WebClient: non-blocking, reactive HTTP kliens
* Jól használható Websocket és SSE esetén

---

## Spring WebFlux

<img src="images/Non-blocking-request-processing.png" alt="Non-blocking request processing" width="900" />

---

class: inverse, center, middle

# Webes fejlesztés - lekérdezés

---

## Controller

```java
@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {

    private EmployeeService employeeService;

    @GetMapping
    public Flux<EmployeeResource> employees() {
        return employeeService.listEmployees();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeResource>> findEmployeeById(@PathVariable("id") long id) {
        return employeeService
          .findEmployeeById(id)
          .map(ResponseEntity::ok)
          .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
```

---

## Service réteg

```java
@Service
@AllArgsConstructor
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public Flux<EmployeeResource> listEmployees() {
        return employeeRepository.findAll().map(employeeMapper::toEmployeeResource);
    }

    public Mono<EmployeeResource> findEmployeeById(long id) {
        return employeeRepository.findById(id)
                .map(employeeMapper::toEmployeeResource);
    }

}
```

---

## Repository réteg


```java
@Repository
public class EmployeeRepository {

    private final List<Employee> employees = Collections.synchronizedList(new ArrayList<>());

    public Mono<Employee> findById(long id) {
        return Flux.fromIterable(employees)
                .filter(e -> e.getId() == id).singleOrEmpty();
    }


    public Flux<Employee> findAll() {
        return Flux.fromIterable(employees);
    }
}
```

---

class: inverse, center, middle

# Webes fejlesztés - módosítás

---

## Controller

.small-code-14[
```java
@PostMapping
public Mono<ResponseEntity<EmployeeResource>> createEmployee(@RequestBody Mono<EmployeeResource> command) {
    return employeeService.createEmployee(command)
            .map(employee -> ResponseEntity.created(URI.create(String.format("/api/employees/%d", employee.getId()))).body(employee));
}

@PutMapping("/{id}")
public Mono<ResponseEntity<EmployeeResource>> updateEmployee(@PathVariable("id") long id, 
        @RequestBody Mono<EmployeeResource> command) {
    return employeeService.updateEmployee(id, command)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
}

@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteEmployee(@PathVariable("id") long id) {
    employeeService.deleteEmployee(id);
}
```
]

---

## Service

```java
public Mono<EmployeeResource> createEmployee(Mono<EmployeeResource> command) {
    return command
            .map(employeeMapper::toEmployee)
            .flatMap(employeeRepository::save)
            .map(employeeMapper::toEmployeeResource);
}

public Mono<EmployeeResource> updateEmployee(long id, Mono<EmployeeResource> command) {
    return command
        .zipWith(employeeRepository.findById(id))
        .doOnNext(t -> t.getT2().setName(t.getT1().getName()))
        .map(t -> t.getT2())
        .flatMap(e -> employeeRepository.save(e))
        .map(employeeMapper::toEmployeeResource);
}

public Mono<Void> deleteEmployee(long id) {
    return employeeRepository.deleteById(id);
}
```

---

## Repository

.small-code-14[
```java
private final AtomicLong idSequence = new AtomicLong();

public Mono<Employee> save(Employee employee) {
    if (employee.getId() == null) {
        return Mono.just(employee)
                .doOnNext(e -> employee.setId(idSequence.incrementAndGet()))
                .doOnNext(employees::add);
    }
    else {
        return Flux
                .just(employee)
                .filter(e -> e.getId().equals(employee.getId()))
                .doOnNext(e -> e.setName(employee.getName()))
                .singleOrEmpty();
    }
}

public Mono<Void> deleteById(long id) {
    employees.removeIf(e -> e.getId() == id);
    return Mono.empty();
}
```
]

---

class: inverse, center, middle

# Validáció

---

## Validáció

* Jakarta Validation, `org.springframework.boot:spring-boot-starter-validation` függőség
* `@Valid` annotáció

---

## Problem Details


```java
@ControllerAdvice
public class EmployeesExceptionHandler {

    @ExceptionHandler
    public Mono<ProblemDetail> handle(WebExchangeBindException exception) {
        return Mono.just(exception)
                .map(e -> {
                    var problemDetail = ProblemDetail
                        .forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
                    problemDetail.setType(URI.create("employees/validation-error"));
                    problemDetail.setTitle("Validation error");
                    List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                        .map((FieldError fe) -> new Violation(fe.getField(), fe.getDefaultMessage()))
                        .toList();
                    problemDetail.setProperty("violations", violations);
                    return problemDetail;
                });

    }

}
```

---

class: inverse, center, middle

# RouterFunction

---

## RouterFunction - lekérdezés

.small-code-14[
```java
@Configuration
@AllArgsConstructor
public class EmployeeHandler {

    private EmployeeService employeeService;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(RequestPredicates.GET("/api/employees"), this::employees)
                .andRoute(RequestPredicates.GET("/api/employees/{id}"), this::findEmployeeById)
    }

    public Mono<ServerResponse> employees(ServerRequest request) {
        return ServerResponse
                .ok()
                .body(employeeService.listEmployees(), EmployeeResource.class);
    }

    public Mono<ServerResponse> findEmployeeById(ServerRequest request) {
        return ServerResponse.ok()
                .body(employeeService.findEmployeeById(Long.parseLong(request.pathVariable("id"))), EmployeeResource.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
```
]

---

## RouterFunction - módosítás

.small-code-12[
```java
@Bean
public RouterFunction<ServerResponse> route() {
    return RouterFunctions
            .route(RequestPredicates.GET("/api/employees"), this::employees)
            .andRoute(RequestPredicates.GET("/api/employees/{id}"), this::findEmployeeById)
            .andRoute(RequestPredicates.POST("/api/employees"), this::createEmployee)
            .andRoute(RequestPredicates.PUT("/api/employees/{id}"), this::updateEmployee)
            .andRoute(RequestPredicates.DELETE("/api/employees/{id}"), this::deleteEmployee);
}

public Mono<ServerResponse> createEmployee(ServerRequest request) {
    return employeeService.createEmployee(request.bodyToMono(EmployeeResource.class))
            .flatMap(employee -> 
                ServerResponse
                    .created(URI.create(String.format("/api/employees/%d", employee.getId())))
                    .bodyValue(employee));
}

public Mono<ServerResponse> updateEmployee(ServerRequest request) {
    return ServerResponse.ok()
            .body(employeeService.updateEmployee(
                Long.parseLong(request.pathVariable("id")), 
                request.bodyToMono(EmployeeResource.class)), 
                EmployeeResource.class);
}

public Mono<ServerResponse> deleteEmployee(ServerRequest request) {
    return employeeService
            .deleteEmployee(Long.parseLong(request.pathVariable("id")))
            .flatMap(v -> ServerResponse.noContent().build())
            .switchIfEmpty(ServerResponse.notFound().build());
}
```
]

---

## Validáció

```java
private Validator validator;

public Mono<ServerResponse> createEmployee(ServerRequest request) {
    return employeeService.createEmployee(
            request
                .bodyToMono(EmployeeResource.class)
                .doOnNext(this::validate))
        .flatMap(employee -> ServerResponse
            .created(URI.create(String.format("/api/employees/%d", employee.getId())))
            .bodyValue(employee));
}
```

---

class: inverse, center, middle

# Webes fejlesztés - Template engine

---

## Thymeleaf

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

---

## Controller

.small-code-12[
```java
@Controller
@Slf4j
@AllArgsConstructor
public class EmployeeWebController {

    private EmployeeService employeeService;

    @GetMapping("/")
    public Mono<Rendering> listEmployees() {
        return Mono.just(
                Rendering.view("index")
                        .modelAttribute("employees", employeeService.listEmployees())
                        .modelAttribute("command", Mono.just(new EmployeeResource()))
                        .build()
        );
    }

    @PostMapping("/")
    public Mono<Rendering> createUserPost(Mono<EmployeeResource> command) {
        // Nincs Flash attribute: https://github.com/spring-projects/spring-framework/issues/20575
        return employeeService
                .createEmployee(command).map(user -> Rendering.redirectTo("/").build());
    }

}
```
]

---

class: inverse, center, middle

# SSE

---

## SSE

```java
@RestController
@RequestMapping("/api/counter")
public class CounterController {

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> numbers() {

        return Flux.range(0, 10)
                .delayElements(Duration.ofSeconds(2))
                .map(this::event);
    }

    public ServerSentEvent<String> event(int i) {
        return  ServerSentEvent.<String> builder()
                .id(String.valueOf(i))
                .event("periodic-event")
                .data("SSE - " + LocalTime.now().toString())
                .build();
    }
}
```

---

class: inverse, center, middle

# Adatbáziskezelés R2DBC használatával

---

## Klasszikus RDBMS réteg

* JDBC
* Szinkron, blokkoló

---

## Reactive RDBMS

* R2DBC nyílt specifikáció
* Project Reactor és RxJava támogatás
* Implementációk: H2, MySQL/MariaDB, PostgreSQL, Oracle, Microsoft SQL Server
* Klasszikus funkciók: tranzakciókezelés, batch, LOB, unchecked és típusos kivételek
* Tudja használni pl. a Spring Data R2DBC vagy JOOQ
* Direktben nem érdemes használni

---

## Postgresql indítása

.small-code-12[
```shell
docker run -d -e POSTGRES_DB=employees -e POSTGRES_USER=employees -e POSTGRES_PASSWORD=employees -p 5432:5432 --name employees-postgres postgres
```
]

---

## Spring Data R2DBCP

* `org.springframework.boot:spring-boot-starter-data-r2dbc` függőség
* Driver: `org.postgresql:r2dbc-postgresql`

```properties
spring.r2dbc.url=r2dbc:postgresql://localhost/employees
spring.r2dbc.username=employees
spring.r2dbc.password=employees
spring.r2dbc.initialization-mode=always
```

---

## Spring Data R2DBC <br /> Repository

Repo:

```java
public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {

}
```

---

## Séma inicializálás

```java
@Bean
public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

  ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
  initializer.setConnectionFactory(connectionFactory);

  CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
  populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
  initializer.setDatabasePopulator(populator);

  return initializer;
}
```

---

## CQRS

```java
@Query("select id, name from employee")
Flux<EmployeeResource> findAllResources();

@Query("select id, name from employee where id = :id")
Mono<EmployeeResource> findResourceById(@Param("id") long id);
```

---

class: inverse, center, middle

# R2DBC migration tool

---

## R2DBC migration tool

```xml
<dependency>
  <groupId>name.nkonev.r2dbc-migrate</groupId>
  <artifactId>r2dbc-migrate-spring-boot-starter</artifactId>
  <version>2.11.0</version>
</dependency>
```

* `src/main/resources/db/migration/V1__employees.sql`

---

class: inverse, center, middle

# Tranzakciókezelés

---

## Tranzakciókezelés

* Támogatott a `@Transactional` annotáció Spring Framework 5.2 M2 óta

```java
@Transactional
public Mono<EmployeeResource> createEmployee(Mono<EmployeeResource> command) {
    return command
            .map(employeeMapper::toEmployee)
            .flatMap(employeeRepository::save)
            .doOnNext(e -> log.info("Created employee: {}", e))
            .map(employeeMapper::toEmployeeResource)
            .map(e -> {throw new IllegalStateException("erter");});
}
```

---

class: inverse, center, middle

# MongoDB

---

## MongoDB elindítása

```shell
docker run -d -p27017:27017 --name employees-mongo mongo
```
---

## Alkalmazás előkészítése

`application.properties` fájlban:

```properties
spring.data.mongodb.database = employees
```

`pom.xml` függőség

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
</dependency>
```

---

## Document

```java
@Document(collection = "employees")
@Data
@NoArgsConstructor
public class Employee {

    @Id
    private String id;

    private String name;

    public Employee(String name) {
        this.name = name;
    }

}

```

---

## Dao réteg - MongoDB

```java
public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {

}
```

---

## Konzol

```shell
docker exec -it employees-mongo mongosh employees
```

```javascript
db.employees.find()

db.employees.insertOne({"name": "John Doe"})

db.employees.findOne({'_id': ObjectId('60780cf974bc5648cf220a96')})

db.employees.deleteOne({'_id': ObjectId('60780cf974bc5648cf220a96')})
```

---

class: inverse, center, middle

# Cache

---

## Cache

* Cache abstraction nem működik
* `cache()`, invalidálás miatt problémás

---

## Redis

* Van reaktív driver

```shell
docker run --name employees-redis -p 6379:6379 -d redis
```

---

## Függőség

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis-reactive</artifactId>
</dependency>
```

* Repo legyen `R2dbcRepository`

---

## Configuration

.small-code-14[
```java
@Configuration(proxyBeanMethods = false)
public class CacheConfig {

    @Bean
    public ReactiveRedisTemplate<Long, EmployeeResource> reactiveRedisTemplate(ReactiveRedisConnectionFactory connectionFactory) {
        ObjectMapper objectMapper = new ObjectMapper();


        RedisSerializer<EmployeeResource> redisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, EmployeeResource.class);
        RedisSerializationContext<Long, EmployeeResource> context = RedisSerializationContext
                .<Long, EmployeeResource>newSerializationContext(RedisSerializer.string())
                .key(new GenericToStringSerializer<>(Long.class))
                .hashKey(new Jackson2JsonRedisSerializer<>(Long.class))
                .value(redisSerializer)
                .hashValue(redisSerializer)
                .build();

        return new ReactiveRedisTemplate<>(connectionFactory, context);
    }
}
```
]

---

## Service

.small-code-14[
```java
public Mono<EmployeeResource> findEmployeeById(long id) {
    return reactiveRedisTemplate.opsForValue().get(id)
            .log()
            .switchIfEmpty(employeeRepository
                    .findResourceById(id)
                    .flatMap(resource -> reactiveRedisTemplate.opsForValue().set(id, resource).thenReturn(resource)))
            .log()
            ;
}

public Mono<EmployeeResource> updateEmployee(long id, Mono<EmployeeResource> command) {
    return command
            .zipWith(employeeRepository.findById(id))
            .doOnNext(t -> t.getT2().setName(t.getT1().getName()))
            .map(t -> t.getT2())
            .flatMap(e -> employeeRepository.save(e))
            .map(employeeMapper::toEmployeeResource)
            .flatMap(resource -> reactiveRedisTemplate.opsForValue().set(id, resource).thenReturn(resource))
            ;
}
```
]

---

class: inverse, center, middle

# Reactive WebClient

---

## Reactive WebClient helye

* Szinkron blokkoló HTTP protokoll
* Reactive WebClient
* `org.springframework.boot:spring-boot-starter-webflux` függőség

---

## Reactive WebClient

```java
private WebClient.Builder webClientBuilder;

private void printResponse() {
    var webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    var response = webClient.get().uri("/api/employees").retrieve().bodyToMono(String.class).block();
    log.info("Response: {}", response);
}
```

```java
private WebClient.Builder webClientBuilder;

private Flux<EmployeesResource> listEmployees() {
    var webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    return webClient.get().uri("/api/employees").retrieve().bodyToFlux(EmployeeResource.class);
}
```

---

class: inverse, center, middle

# HTTP interface

---

## Interfész

```java
@HttpExchange("/api/employees")
public interface EmployeesService {

    @GetExchange
    Flux<EmployeeResource> listEmployees();
}
```

---

## Implementáció

```java

@Bean
public EmployeesService employeesService(WebClient.Builder webClientBuilder) {
    var webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    var factory = HttpServiceProxyFactory
			.builderFor(WebClientAdapter.create(webClient)).build();
	return factory.createClient(EmployeeService.class);
}
```

---

class: inverse, center, middle

# RabbitMQ

---

## RabbitMQ

* Message broker
* AMQP szabványos protokoll
* A legtöbb programozási nyelven kliens
* Erlang
* VMware

---

## RabbitMQ futtatása <br /> Docker konténerben

```shell
docker run -d -p 5672:5672 -p 15672:15672 --name rabbitmq rabbitmq:3-management
```

`http://localhost:15672/`, `guest` / `guest`

```shell
docker exec rabbitmq rabbitmqctl status
```

---

## Függőség

```xml
<dependency>
    <groupId>io.projectreactor.rabbitmq</groupId>
    <artifactId>reactor-rabbitmq</artifactId>
  </dependency>
```

---

## Konfiguráció

.small-code-10[
```java
@Configuration
public class RabbitMqConfig {

    public static final String MESSAGES_QUEUE = "messages";

    @Autowired
    private AmqpAdmin admin;

    @Bean
    public Mono<Connection> connectionMono(RabbitProperties rabbitProperties) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(rabbitProperties.getHost());
        connectionFactory.setPort(rabbitProperties.getPort());
        connectionFactory.setUsername(rabbitProperties.getUsername());
        connectionFactory.setPassword(rabbitProperties.getPassword());
        return Mono.fromCallable(() -> connectionFactory.newConnection("reactor-rabbit")).cache();
    }

    @Bean
    public Sender sender(Mono<Connection> connectionMono) {
        return RabbitFlux.createSender(new SenderOptions().connectionMono(connectionMono));
    }

    @Bean
    public Receiver receiver(Mono<Connection> connectionMono) {
        return RabbitFlux.createReceiver(new ReceiverOptions().connectionMono(connectionMono));
    }

    @Bean
    public Flux<Delivery> delivery(Receiver receiver) {
        return receiver.consumeAutoAck(MESSAGES_QUEUE);
    }

    @PostConstruct
    public void init() {
        admin.declareQueue(new Queue(MESSAGES_QUEUE, false, false, true));
    }
}
```
]

---

## Küldés

.small-code-14[
```java
@Service
@AllArgsConstructor
@Slf4j
public class RabbitMqSender {

    private ObjectMapper objectMapper;

    private Sender sender;

    public Mono<Void> sendMessage(Mono<EmployeeResource> employee) {
        return sender
                .send(employee
                        .map(item -> item.getName() + " created")
                        .map(s -> new Message(s))
                        .doOnNext(m -> log.info("Sending message: {}", m))
                        .map(this::convertToJson)
                        .map(bytes -> new OutboundMessage("", MESSAGES_QUEUE, bytes)));
    }

    @SneakyThrows
    private byte[] convertToJson(Message s) {
        return objectMapper.writeValueAsBytes(s);
    }
}
```
]

---

## Küldés meghívása

```java
public Mono<EmployeeResource> createEmployee(Mono<EmployeeResource> command) {
    return command
            .log("Create employee") // paraméter a category, itt commandot írja ki
            .map(employeeMapper::toEmployee)
            .flatMap(employeeRepository::save)
            .doOnNext(e -> log.info("Created employee: {}", e))
            .map(employeeMapper::toEmployeeResource)
            .log("Create employee") // itt a dto-t írja ki
            .flatMap(e -> sender.sendMessage(Mono.just(e)).thenReturn(e));
}
```

---

## Fogadás

.small-code-14[
```java
@Service
@Slf4j
public class RabbitMqReceiver {

    ObjectMapper objectMapper;
    private Flux<Message> messages;

    public RabbitMqReceiver(ObjectMapper objectMapper, Flux<Delivery> delivery) {
        this.objectMapper = objectMapper;
        messages = delivery.map(d -> d.getBody())
                .map(this::parseFromJson)
                .share();

        messages.subscribe(message -> log.info("Message has come: {}", message));
    }

    @SneakyThrows
    private Message parseFromJson(byte[] content) {
        return objectMapper.readValue(content, Message.class);
    }

    public Flux<Message> getMessages() {
        return messages;
    }
}
```
]

---

## Controller

```java
@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessagesController {

    private RabbitMqReceiver receiver;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Message>> messages() {
        return Flux.from(receiver.getMessages().map(this::event
                ));
    }

    private ServerSentEvent<Message> event(Message message) {
        return  ServerSentEvent.<Message> builder()
                .id(UUID.randomUUID().toString())
                .event("message-event")
                .data(message)
                .build();
    }
}
```

---

class: inverse, center, middle

# Spring Cloud Stream, Kafka

---

## Kafka indítása

```shell
cd kafka
docker compose up -d
```

---

## Spring Cloud Stream

```xml
<spring-cloud.version>2023.0.0</spring-cloud.version>
```

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-stream-binder-kafka-reactive</artifactId>
</dependency>
```

---

## Üzenetküldés

* Üzleti logikából `StreamBridge` használatával

```java
public class EmployeeService {

    // ...

    private StreamBridge streamBridge;

    public Mono<EmployeeResource> createEmployee(Mono<EmployeeResource> command) {
        return command
                .map(employeeMapper::toEmployee)
                .flatMap(employeeRepository::save)
                .map(employeeMapper::toEmployeeResource)
                .doOnNext(e -> streamBridge.send("employeesTopic", e));
    }
    
}
```

```properties
spring.cloud.stream.bindings.employeesTopic.destination=employees-topic
```

---

## Üzenet fogadása

* `subscribe()` hívása kötelező!

```java
@Configuration(proxyBeanMethods = false)
@Slf4j
public class EmployeesGateway {

    @Bean
    public Consumer<Flux<EmployeeResource>> handleEmployee() {
        return employee -> employee.subscribe(e -> log.info("Employee has been created: {}", e));
    }

}
```

```properties
spring.cloud.function.definition=handleEmployee
spring.cloud.stream.bindings.handleEmployee-in-0.destination=employees-topic
```

---

## Küldés és fogadás

```java
@Configuration(proxyBeanMethods = false)
@Slf4j
public class EmployeesGateway {

    @Bean
    public Function<Flux<EmployeeResource>, Flux<EmployeeResource>> handleEmployee() {
        return employeeFlux -> employeeFlux
                .doOnNext(e -> log.info("Got, send reply: {}", e))
                .map(e -> new EmployeeResource(e.getId(), "Reply: " + e.getName()));
    }

}
```

```properties
spring.cloud.function.definition=handleEmployee
spring.cloud.stream.bindings.handleEmployee-in-0.destination=employees-topic
spring.cloud.stream.bindings.handleEmployee-out-0.destination=employees-reply-topic
```

---

class: inverse, center, middle

# RSocket

---

## RSocket

* Reactive Streams semantics between client-server, and server-server communication
* Binary protocol for use on byte stream transports such as TCP, WebSockets (reactor-netty), akka, Aeron (UDP)
* Különböző kommunikációs modellek [Introduction to RSocket](https://www.baeldung.com/rsocket)
  * Request/Response
  * Fire-and-Forget
  * Request/Stream (egy kérés, több válasz)
  * Channel (kétirányú adatfolyam)
* Java, Kotlin, JavaScript, Go, .NET, C++
* Natív Spring támogatás
* Spring server: [Getting Started With RSocket: Spring Boot Server](https://spring.io/blog/2020/03/02/getting-started-with-rsocket-spring-boot-server)
* Spring client: [Getting Started With RSocket: Spring Boot Client](https://spring.io/blog/2020/03/09/getting-started-with-rsocket-spring-boot-client)

---

## Ajánlott irodalom

* Craig Walls: Spring in Action (5. edition)
* Josh Long: Reactive Spring
* [GOTO 2019 • Reactive Spring • Josh Long](https://www.youtube.com/watch?v=1F10gr2pbvQ)