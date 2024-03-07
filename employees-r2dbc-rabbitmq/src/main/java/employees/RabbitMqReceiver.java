package employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Delivery;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
public class RabbitMqReceiver {

    private ObjectMapper objectMapper;

    @Getter
    private Flux<EmployeeResource> employees;
    public RabbitMqReceiver(ObjectMapper objectMapper, Flux<Delivery> delivery) {
        this.objectMapper = objectMapper;
        employees = delivery.map(d -> d.getBody())
                .map(this::parseFromJson)
                .share();

                employees.subscribe(message -> log.info("Message has come: {}", message));
    }

    @SneakyThrows
    private EmployeeResource parseFromJson(byte[] content) {
        return objectMapper.readValue(content, EmployeeResource.class);
    }

}
