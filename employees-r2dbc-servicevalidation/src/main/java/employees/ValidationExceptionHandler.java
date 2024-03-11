package employees;

import jakarta.validation.ConstraintViolationException;
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
//    @ExceptionHandler
//    public ProblemDetail handleIllegalStateException(IllegalStateException illegalStateException) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Short name");
//    }

    @ExceptionHandler
    public ProblemDetail handle(ConstraintViolationException exception) {
        return Optional.of(exception).stream()
                .map(e -> {
                            // e.getMessage()
                            var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Validation error");
                            problem.setType(URI.create("employees/validation-error"));
                            problem.setTitle("Validation error");

//                    var messages = Flux.fromIterable(e.getBindingResult().getFieldErrors())
//                            .map(fieldError -> new FieldMessage(fieldError.getField(), fieldError.getDefaultMessage())).collectList().block();

                            var messages = e.getConstraintViolations()
                                    .stream()
                                    .map(fieldError -> new FieldMessage(fieldError.getPropertyPath().toString(), fieldError.getMessage()))
                                    .toList();

                            problem.setProperty("messages", messages);

                            return problem;
                        }
                ).findAny().orElseThrow();
    }

    @ExceptionHandler
    public ProblemDetail handle(EmployeeAlreadyExistsException e) {
        var result = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Business validation error");
        result.setTitle("Employee already exists");
        result.setType(URI.create("employees/employee-already-exists"));
        result.setDetail(e.getMessage());
        return result;
    }



    // TODO JSON syntax error
}
