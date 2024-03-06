package employees;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class EmployeeRepository {

    private final List<Employee> employees = List.of(
            new Employee(1L, "John Doe"),
            new Employee(2L, "Jack Doe")
    );

    public Flux<Employee> findAll() {
        return Flux.fromIterable(employees);
    }

    public Mono<Employee> findById(long id) {
        return Flux.fromIterable(employees)
                .filter(e -> e.getId() == id)
                .singleOrEmpty();
    }
}
