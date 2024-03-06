package employees;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

public class EmployeesMain {

    public static void main(String[] args) {
        // Mono: 0 vagy 1 elem van: Optionalhöz hasonlatos
        // Flux: 0 vagy n elem van: Streamhez hasonlatos

//        Flux.just(1, 2, 3, 4) // Stream.of
//                .doOnNext(i -> System.out.println(i)) // peek()
//                .subscribe(i -> System.out.println("Subscribe: " + i)); // forEach()

//        List<Integer> numbers = List.of(1, 2, 3, 4);
//        Flux.fromIterable(numbers)
//                .subscribe(i -> System.out.println(i));

//        Mono.just(6)
//                .subscribe(i -> System.out.println(i));

//        Mono.empty()
//                .subscribe(i -> System.out.println(i));


    }
}
