package employees;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

//@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeesController {

    private EmployeesService employeesService;

    @GetMapping
    public Flux<EmployeeResource> employees() {
        return employeesService.listEmployees();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<EmployeeResource>> findEmployeeById(@PathVariable("id") String id) {
        return employeesService
                .findEmployeeById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Mono<ResponseEntity<EmployeeResource>> createEmployee(@Valid @RequestBody Mono<EmployeeResource> employeeResource) {
        return employeesService
                .createEmployee(employeeResource)
                .map(e -> ResponseEntity.created(URI.create("/api/employees/%s".formatted(e.getId()))).body(e));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<EmployeeResource>> updateEmployee(@PathVariable("id") String id, @RequestBody EmployeeResource employeeResource) {
        return employeesService.updateEmployee(id, employeeResource)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteEmployee(@PathVariable("id") String id) {
        return employeesService.deleteEmployee(id);
    }
}
