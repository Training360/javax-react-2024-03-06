package employees;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public Mono<ResponseEntity<EmployeeResource>> findEmployeeById(@PathVariable("id") long id) {
        return employeesService
                .findEmployeeById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<EmployeeResource> createEmployee(@RequestBody Mono<EmployeeResource> employeeResource) {
        return employeesService.createEmployee(employeeResource);
    }
}
