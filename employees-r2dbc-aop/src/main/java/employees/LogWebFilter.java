package employees;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class LogWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var now = System.currentTimeMillis();

        var result = chain.filter(exchange);

        log.info("Request: {}, time: {}", exchange.getRequest().getPath(), System.currentTimeMillis() - now);
        return result;
    }
}
