package employees;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class EmployeesMessagesController {

    private RabbitMqReceiver receiver;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<EmployeeResource>> messages() {
        return Flux.from(receiver.getEmployees().map(this::toEvent));
    }

    private ServerSentEvent<EmployeeResource> toEvent(EmployeeResource message) {
        return  ServerSentEvent.<EmployeeResource> builder()
                .id(UUID.randomUUID().toString())
                .event("message-event")
                .data(message)
                .build();
    }

}
