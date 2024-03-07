package employees;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Configuration
@AllArgsConstructor
public class EmployeeHandler {

    private EmployeesService employeesService;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(RequestPredicates.GET("/api/employees"), this::employees)
                .andRoute(RequestPredicates.GET("/api/employees/{id}"), this::findEmployeeById)

                ;
    }

    public Mono<ServerResponse> employees(ServerRequest serverRequest) {
        return
                ServerResponse.ok().body(employeesService.listEmployees(), EmployeeResource.class);
    }

    public Mono<ServerResponse> findEmployeeById(ServerRequest serverRequest) {
        return ServerResponse.ok()
                .body(employeesService.findEmployeeById(Long.parseLong(serverRequest.pathVariable("id"))), EmployeeResource.class)
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
