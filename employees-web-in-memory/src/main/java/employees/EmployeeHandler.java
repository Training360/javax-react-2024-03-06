package employees;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@Configuration
@AllArgsConstructor
public class EmployeeHandler {

    private EmployeesService employeesService;

    @Bean
    public RouterFunction<ServerResponse> route() {
        return RouterFunctions
                .route(RequestPredicates.GET("/api/employees"), this::employees)
                .andRoute(RequestPredicates.GET("/api/employees/{id}"), this::findEmployeeById)
                .andRoute(RequestPredicates.POST("/api/employees"), this::createEmployee)
                .andRoute(RequestPredicates.PUT("/api/employees/{id}"), this::updateEmployee)
                .andRoute(RequestPredicates.DELETE("/api/employees/{id}"), this::deleteEmployee)
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

    public Mono<ServerResponse> createEmployee(ServerRequest request) {
        return employeesService.createEmployee(request.bodyToMono(EmployeeResource.class))
                .flatMap(employee ->
                        ServerResponse
                                .created(URI.create(String.format("/api/employees/%d", employee.getId())))
                                .bodyValue(employee));
    }
    public Mono<ServerResponse> updateEmployee(ServerRequest request) {
        return ServerResponse.ok()
                .body(
                        request.bodyToMono(EmployeeResource.class)
                                        .flatMap(e -> employeesService.updateEmployee(
                                                Long.parseLong(request.pathVariable("id")),
                                                e)), EmployeeResource.class);
    }
    public Mono<ServerResponse> deleteEmployee(ServerRequest request) {
        return employeesService
                .deleteEmployee(Long.parseLong(request.pathVariable("id")))
                .then(ServerResponse.noContent().build());
    }

}
