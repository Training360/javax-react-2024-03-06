package employees;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeesService {

    private EmployeeRepository employeeRepository;

    public Flux<EmployeeResource> listEmployees() {
//        return employeeRepository.findAll()
//                .map(this::toResource);
        return employeeRepository.findAllResources();
    }

    public Mono<EmployeeResource> findEmployeeById(long id) {
//        return employeeRepository.findById(id)
//                .map(this::toResource);
        return employeeRepository.findResourceById(id);
    }

    private EmployeeResource toResource(Employee employee) {
        return new EmployeeResource(employee.getId(), employee.getName());
    }

    public Mono<EmployeeResource> createEmployee(Mono<EmployeeResource> employeeResource) {
        return employeeResource
                .doOnNext(this::validate)
                .map(resource -> new Employee(resource.getName()))
                .flatMap(employeeRepository::save)
                .map(this::toResource);
    }

    public void validate(EmployeeResource employeeResource) {
        if (employeeResource.getName().length() < 2) {
            throw new IllegalStateException("Short name");
        }
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
