package employees;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;

public record Employee(@Id Long id, String name) {

    public Employee( String name) {
        this(null, name);
    }
}
