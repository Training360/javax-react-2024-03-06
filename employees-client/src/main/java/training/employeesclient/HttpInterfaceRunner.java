package training.employeesclient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@AllArgsConstructor
@Slf4j
public class HttpInterfaceRunner implements CommandLineRunner {

    private EmployeeService employeeService;

    @Override
    public void run(String... args) throws Exception {
//        var employees = employeeService.listEmployees().collectList().block();
//        log.info("Result: {}", employees);

        Flux.just(14, 15, 16)
                .flatMap(i -> employeeService.findEmployeeById(i))
                .doOnNext(e -> log.info("Employee: {}", e))
                .blockLast();
    }
}
