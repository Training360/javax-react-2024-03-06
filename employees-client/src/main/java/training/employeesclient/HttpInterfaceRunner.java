package training.employeesclient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class HttpInterfaceRunner implements CommandLineRunner {

    private EmployeeService employeeService;

    @Override
    public void run(String... args) throws Exception {
        var employees = employeeService.listEmployees().collectList().block();
        log.info("Result: {}", employees);
    }
}
