package training.employeesclient;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@AllArgsConstructor
@Slf4j
public class WebClientRunner implements CommandLineRunner {

    private WebClient.Builder builder;

    @Override
    public void run(String... args) throws Exception {
        var webClient = builder.baseUrl("http://localhost:8080").build();
        var response = webClient
                .get()
                .uri("/api/employees")
                .retrieve()
                .bodyToFlux(Employee.class)
//                .subscribe(s -> log.info("Result: {}", s));
                .collectList()
                .block();
        log.info("Result: {}", response);
    }
}
