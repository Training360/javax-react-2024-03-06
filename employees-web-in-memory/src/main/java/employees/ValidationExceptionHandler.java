package employees;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;

@ControllerAdvice
public class ValidationExceptionHandler {

    @ExceptionHandler
    public ProblemDetail handle(WebExchangeBindException exception) {
        return Optional.of(exception).stream()
                .map(e -> {
                    // e.getMessage()
                    var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation error");
                    problem.setType(URI.create("employees/validation-error"));
                    problem.setTitle("Validation error");

//                    var messages = Flux.fromIterable(e.getBindingResult().getFieldErrors())
//                            .map(fieldError -> new FieldMessage(fieldError.getField(), fieldError.getDefaultMessage())).collectList().block();

                    var messages = e.getBindingResult()
                            .getFieldErrors()
                            .stream()
                            .map(fieldError -> new FieldMessage(fieldError.getField(), fieldError.getDefaultMessage()))
                                    .toList();

                    problem.setProperty("messages", messages);

                    return problem;
                        }
                        ).findAny().orElseThrow();
    }



    // TODO JSON syntax error
}
