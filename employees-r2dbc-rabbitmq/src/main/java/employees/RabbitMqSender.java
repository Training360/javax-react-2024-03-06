package employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.rabbitmq.OutboundMessage;
import reactor.rabbitmq.Sender;

import static employees.RabbitMqConfig.MESSAGES_QUEUE;

@Service
@AllArgsConstructor
@Slf4j
public class RabbitMqSender {

    private ObjectMapper objectMapper;

    private Sender sender;

    public Mono<Void> sendMessage(Mono<EmployeeResource> employee) {
        return sender.send(employee
                .map(this::convertToJson)
                .map(bytes -> new OutboundMessage("", MESSAGES_QUEUE, bytes)));
    }

    @SneakyThrows
    private byte[] convertToJson(EmployeeResource s) {
        return objectMapper.writeValueAsBytes(s);
    }
}
