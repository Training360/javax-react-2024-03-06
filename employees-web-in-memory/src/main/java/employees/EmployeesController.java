package employees;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeesController {

    private EmployeesService employeesService;

    @GetMapping
    public Flux<EmployeeResource> employees() {
        return employeesService.listEmployees();
    }

    @GetMapping("/{id}")
    public Mono<EmployeeResource> findEmployeeById(@PathVariable("id") long id) {
        return employeesService.findEmployeeById(id);
    }
}
