package employees;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class ErrorsMain {

    public static void main(String[] args) {
        Flux.just(new Employee(1L, "Joe", List.of()),
                new Employee(2L, "Joe Joe Joe Joe Joe Joe Joe Joe", List.of())
        )
                .flatMap(
                        e -> Mono.just(e).map(e1 -> e1.subName(10, 15))
                                .onErrorResume(ErrorsMain::isError, t -> Mono.just("unknown"))
                                .onErrorResume(t -> Mono.just("hiba a hibában"))
                )

                .subscribe(System.out::println);
    }

    public static boolean isError(Throwable t) {
        throw new IllegalStateException("nem jó");
    }
}
