package training.employeesclient;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@HttpExchange("/api/employees")
public interface EmployeeService {

    @GetExchange
    Flux<Employee> listEmployees();

    @GetExchange("/{id}")
    public Mono<Employee> findEmployeeById(@PathVariable("id") long id);
}
