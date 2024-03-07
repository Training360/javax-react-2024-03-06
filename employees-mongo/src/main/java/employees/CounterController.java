package employees;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.UUID;

@RestController
@RequestMapping("/api/counter")
@AllArgsConstructor
public class CounterController {

    private ObjectMapper objectMapper;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> count() {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(2))
                .map(i -> "John Doe " + i)
                .map(s -> new EmployeeResource("abcd", s))
                .map(e -> {
                    try {
                        return objectMapper.writeValueAsString(e);
                    }catch (Exception ex) {
                        throw new IllegalStateException("Cannot jsonize", ex);
                    }
                        }
                )
                .map(i ->
                     ServerSentEvent.<String>builder()
                            .id(UUID.randomUUID().toString())
                            .event(i)
                            .comment("ez egy employee")
                            .build()


                        );
    }
}
