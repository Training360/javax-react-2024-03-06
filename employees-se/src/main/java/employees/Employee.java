package employees;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Employee {

    private Long id;

    private String name;

    private List<String> skills;

    public String subName(int start, int end) {
        return name.substring(start, end);
    }
}
