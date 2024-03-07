package employees;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private Long id;

    private String name;

    public Employee(String name) {
        this.name = name;
    }
}
