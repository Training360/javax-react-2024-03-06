package employees;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.net.URI;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler
    public Mono<ProblemDetail> handle(WebExchangeBindException exception) {
        return Mono.just(exception)
                .map(e -> {
                    var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
                    problem.setType(URI.create("employees/validation-error"));
                    problem.setTitle("Validation error");

                    return problem;
                        }
                        );
    }
}
