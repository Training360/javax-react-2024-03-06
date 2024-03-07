package employees;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

@Value
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
@With
public class Employee {

    @Id
    Long id;

    String name;

    public Employee(String name) {
        this.id = null;
        this.name = name;
    }



}
