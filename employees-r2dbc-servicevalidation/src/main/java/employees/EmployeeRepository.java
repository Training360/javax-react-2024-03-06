package employees;

import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeRepository extends ReactiveCrudRepository<Employee, Long> {

//    @Query("select id, name from employee") // SQL
    Flux<EmployeeResource> findAllBy();

//    @Query("select id, name from employee where id = :id")
    // @Param("id")
    Mono<EmployeeResource> findResourceById( long id);

    Mono<Boolean> existsByName(String name);
}
