package employees;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@Slf4j
@AllArgsConstructor
public class EmployeesWebController {

    private EmployeesService employeesService;

    @GetMapping("/")
    public Mono<Rendering> listEmployees() {
        return Mono.just(
                Rendering.view("index")
                        .modelAttribute("employees", employeesService.listEmployees())
                        .modelAttribute("command", Mono.just(new EmployeeResource()))
                        .build()
        );
    }

    @PostMapping
    public Mono<Rendering> createEmployee(Mono<EmployeeResource> employeeResource) {
        return employeesService.createEmployee(employeeResource).map(resource -> Rendering.redirectTo("/").build());
    }

}
