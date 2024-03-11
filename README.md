# Reaktív programozás Spring Boot használatával

---

class: inverse, center, middle

# Publisherek típusai, backpressure

---

## Hot publisher

* Azonnal létrehozza az elemeket, anélkül hogy feliratkozna egy subscriber
* Elemeket _eager_ módon hozza létre
* Nem ismételhető
* Pl. egérmozgatás, HTTP kérések kiszolgálása

```java
var values = Mono.just(getInitialValue());

public static int getInitialValue() {
    System.out.println("getInitialValue");
    return 5;
}
```

* Metódus `subscribe()` nélkül is lefut
* Két `subscribe()` esetén a második nem kap elemet

---

## Cold publisher

* Csak akkor hozza létre az elemeket, ha feliratkozik egy vagy több subscriber
* Elemeket _lazy_ módon hozza létre
* Ismételhető
* Pl. HTTP kérések küldése
* Hot publisher cold típusúvá alakítható a `defer()` metódussal

```java
var values = Mono.defer(() -> Mono.just(getInitialValue()));
```

* Metódus csak `subscribe()` hívással fut le
* Két `subscribe()` esetén a második is megkapja az elemet
* Coldból hot alakítható: `share()`, `replay()`

---

class: inverse, center, middle

# Backpressure

---

## Backpressure

* Mechanizmus, mely akkor alkalmazandó, ha túl sok elem jön be a consumerhez a publishertől
* Technikák: throttling, windows, buffers, dropping

---

## Automatikus <br /> backpressure kezelés

```java
Flux.range(1, Integer.MAX_VALUE)
        .log()
        .concatMap(x -> Mono.delay(Duration.ofMillis(100)), 1) // simulate that processing takes time
        .blockLast();
```

* A `range()` publisher képes olyan ütemben adni az elemeket, ahogy a consumer kéri

---

## Nincs backpressure kezelés

```java
Flux.interval(Duration.ofMillis(1))
        .onBackpressureDrop()
        .log()
        .concatMap(x -> Mono.delay(Duration.ofMillis(100)))
        .blockLast();
```

```plain
onError(reactor.core.Exceptions$OverflowException: Could not emit tick 1 due to lack of requests 
    (interval doesn't support small downstream requests that replenish slower than the ticks))
```

* Az `interval()` publisher __nem__ képes olyan ütemben adni az elemeket, ahogy a consumer kéri

---

## Explicit backpressure kezelés

```java
Flux.interval(Duration.ofMillis(1))
        .onBackpressureDrop()
        .log()
        .concatMap(x -> Mono.delay(Duration.ofMillis(100)))
        .blockLast();
```

* Kihagy elemeket

---


class: inverse, center, middle

# AOP

---

## AOP

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

`@EnableAspectJAutoProxy` annotáció

---

## Aspect

```java
@Component
@Aspect
@Slf4j
public class LogAspect {

    @Around("execution(* employees.EmployeesService.*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinpoint) throws Throwable {
            var start = System.currentTimeMillis();
            var result = joinpoint.proceed();
            log.info("Method: {}, time: {}", joinpoint.getSignature().getName(), System.currentTimeMillis() - start);
            return result;
    }

}
```

---

## Visszatérési érték használata

```java
@Around("execution(* employees.EmployeesService.*(..))")
public Object upperEmployeesName(ProceedingJoinPoint joinpoint) throws Throwable {
    return ((Flux<EmployeeResource>) joinpoint.proceed())
            .doOnNext(e -> e.setName(e.getName().toUpperCase()));
}
```

---

class: inverse, center, middle

# WebFilter

---

## WebFilter

```java
@Component
@Slf4j
public class LogWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var now = System.currentTimeMillis();

        var result = chain.filter(exchange);

        log.info("Request: {}, time: {}", exchange.getRequest().getPath(), System.currentTimeMillis() - now);
        return result;
    }
}
```
