package employees;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
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

    private Validator validator;

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
        return employeesService.findEmployeeById(Long.parseLong(serverRequest.pathVariable("id")))
                .flatMap(e -> ServerResponse.ok().bodyValue(e))
                .switchIfEmpty(ServerResponse.notFound().build());

//        return ServerResponse.ok()
//                .body(employeesService.findEmployeeById(Long.parseLong(serverRequest.pathVariable("id"))), EmployeeResource.class)
//                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createEmployee(ServerRequest request) {
        return employeesService.createEmployee(
                request.bodyToMono(EmployeeResource.class)
                        .doOnNext(this::validate)
                )
                .flatMap(employee ->
                        ServerResponse
                                .created(URI.create(String.format("/api/employees/%d", employee.getId())))
                                .bodyValue(employee));
    }

    private void validate(EmployeeResource employeeResource) {
        var violations = validator.validate(employeeResource);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Mono<ServerResponse> updateEmployee(ServerRequest request) {
        return ServerResponse.ok()
                .body(
                        request.bodyToMono(EmployeeResource.class)
                                        .flatMap(e -> employeesService.updateEmployee(
                                                Long.parseLong(request.pathVariable("id")),
                                                e)), EmployeeResource.class).switchIfEmpty(ServerResponse.notFound().build());
    }
    public Mono<ServerResponse> deleteEmployee(ServerRequest request) {
        return employeesService
                .deleteEmployee(Long.parseLong(request.pathVariable("id")))
                .then(ServerResponse.noContent().build());
    }

}
