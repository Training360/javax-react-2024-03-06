package employees;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class EmployeesService {

    private EmployeeRepository employeeRepository;

    public Flux<EmployeeResource> listEmployees() {
//        return employeeRepository.findAll()
//                .map(this::toResource);
        return employeeRepository.findAllBy();
    }

    public Mono<EmployeeResource> findEmployeeById(long id) {
//        return employeeRepository.findById(id)
//                .map(this::toResource);
        return employeeRepository
                .findResourceById(id)

                // Demó célzattal
//                .delayElement(Duration.ofMillis(new Random().nextInt(3000)))
//                .delayElement(Duration.ofMillis(3000))

                .doOnNext(e -> log.info("Query: {}", e))
                ;
    }

    private EmployeeResource toResource(Employee employee) {
        return new EmployeeResource(employee.getId(), employee.getName());
    }

//    @Transactional
    public Mono<EmployeeResource> createEmployee(Mono<EmployeeResource> employeeResource) {
        return employeeResource
                .flatMap(e -> validate(e).thenReturn(e))
                .map(resource -> new Employee(resource.getName()))
                .flatMap(employeeRepository::save)
                .map(this::toResource)
//                .map(e -> {throw new IllegalStateException("Testing transactional");})
                ;
    }

    public Mono<Void> validate(EmployeeResource employeeResource) {
        return employeeRepository
                .existsByName(employeeResource.getName())
                .handle((alreadyExists, sink) -> {
                    if (alreadyExists) {
                        sink.error(new EmployeeAlreadyExistsException("Already exists with name: %s".formatted(employeeResource.getName())));
                    }
                });
    }

//    public Mono<EmployeeResource> updateEmployee(long id, Mono<EmployeeResource> employeeResource) {
//        return employeeResource
//                .zipWith(employeeRepository.findById(id))
//                .doOnNext(touple -> touple.getT2().setName(touple.getT1().getName()))
//                .map(touple -> touple.getT2())
//                .flatMap(employee -> employeeRepository.save(employee.getT2()))
//                .map(this::toResource);
//    }

public Mono<EmployeeResource> updateEmployee(long id, EmployeeResource employeeResource) {
    return employeeRepository.findById(id)
            .doOnNext(employee -> employee.setName(employeeResource.getName()))
            .flatMap(employee -> employeeRepository.save(employee))
            .map(this::toResource);
}

    public Mono<Void> deleteEmployee(long id) {
        return employeeRepository.deleteById(id);
    }
}
