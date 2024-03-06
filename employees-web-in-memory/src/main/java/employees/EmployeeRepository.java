package employees;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EmployeeRepository {

    private AtomicLong sequenceGenerator = new AtomicLong();

    private final List<Employee> employees = Collections.synchronizedList(new ArrayList<>(List.of(
            new Employee(sequenceGenerator.incrementAndGet(), "John Doe"),
            new Employee(sequenceGenerator.incrementAndGet(), "Jack Doe")
    )));

    public Flux<Employee> findAll() {
        return Flux.fromIterable(employees);
    }

    public Mono<Employee> findById(long id) {
        return Flux.fromIterable(employees)
                .filter(e -> e.getId() == id)
                .singleOrEmpty();
    }

    public Mono<Employee> save(Employee employee) {
        return Mono.just(employee)
                .doOnNext(e -> e.setId(sequenceGenerator.incrementAndGet())) // Nincs mellékhatás
                .doOnNext(employees::add); // Van mellékhatás, kerüljük
    }
}
