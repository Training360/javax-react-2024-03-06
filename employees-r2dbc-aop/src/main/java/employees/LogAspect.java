package employees;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Aspect
@Slf4j
public class LogAspect {

    @Around("execution(* employees.EmployeesService.*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinpoint) throws Throwable {
            var start = System.currentTimeMillis();
            var result = joinpoint.proceed();
            log.info("Method: {}, time: {}", joinpoint.getSignature().getName(), System.currentTimeMillis() - start);
            return result;
    }

    @Around("execution(* employees.EmployeesService.*(..))")
    public Object upperEmployeesName(ProceedingJoinPoint joinpoint) throws Throwable {
        return ((Flux<EmployeeResource>) joinpoint.proceed())
                .doOnNext(e -> e.setName(e.getName().toUpperCase()));
    }

}
