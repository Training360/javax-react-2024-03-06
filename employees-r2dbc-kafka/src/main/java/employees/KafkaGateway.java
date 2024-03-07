package employees;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration(proxyBeanMethods = false)
@Slf4j
public class KafkaGateway {

//    @Bean
//    public Consumer<Flux<EmployeeResource>> handleEmployee() {
//        return employee -> employee.subscribe(e -> log.info("Employee has been created: {}", e));
//    }

    @Bean
    public Function<Flux<EmployeeResource>, Flux<EmployeeResource>> handleEmployee() {
        return employeeFlux -> employeeFlux
                .doOnNext(e -> log.info("Got, send reply: {}", e))
                .map(e -> new EmployeeResource(e.getId(), "Reply: " + e.getName()));
    }
}
