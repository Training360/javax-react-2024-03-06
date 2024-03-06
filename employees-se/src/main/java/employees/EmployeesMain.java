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


        Flux.just(new Employee(1L, "John Doe", List.of("java")),
                new Employee(2L, "Jane Doe", List.of("java", "python")),
                new Employee(3L, "Jack Doe", List.of("java", "c#")))
//                .doOnNext(e -> System.out.println(e))
                .filter(e -> e.getId() > 1)
//                .map(Employee::getName)
//                .log()
                .flatMap(e -> Flux.fromIterable(e.getSkills()))
                .subscribe(System.out::println);
        ;

        Flux.range(0, 10)
                .subscribe(System.out::println);

        List<Integer> list =
            Flux.range(0, 10)
                    .skip(2) // skip()
                    .take(3) // limit()
                .collectList().block();

        System.out.println(list);

        Flux.just(new Employee(1L, "Joe", List.of()),
                new Employee(2L, "Joe Joe Joe Joe Joe Joe Joe Joe", List.of())
                        )
                .map(e -> e.subName(10, 15))
//                .doOnError(t -> t.printStackTrace())
//                .onErrorReturn("unknown")
                //.onErrorResume(t -> Mono.just("unknown"))
                .onErrorContinue((t, e) -> System.out.println("unknown"))
//                .onErrorReturn("unknown")

                .subscribe(System.out::println);


    }
}
