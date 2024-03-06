package employees;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

class EmployeesTest {

    @Test
    void skipReturnNames() {
        var employees = Flux.just(
                new Employee(1L, "John Doe", List.of()),
                new Employee(2L, "Jack Doe", List.of())
        ).skip(1)
                .map(Employee::getName);

        StepVerifier.create(employees)
//                .expectNext("Jack Doe")
                .expectNextMatches(s -> s.startsWith("Jack"))
                .verifyComplete();
    }
}
