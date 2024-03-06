package employees;

import java.util.List;
import java.util.Optional;

public class StreamsMain {

    public static void main(String[] args) {
        List<Long> numbers = List.of(1L, 2L, 3L, 4L, 5L, 6L);
//        var names = numbers.stream().map(StreamsMain::getNameForId).filter(Optional::isPresent).map(Optional::get).toList();
//        System.out.println(names);

        var names = numbers.stream().flatMap(l -> getNameForId(l).stream()).toList();
        System.out.println(names);

                // joe 2, joe 4, joe 6
    }

    public static Optional<String> getNameForId(long id) {
        return id % 2 == 0 ? Optional.of("joe " + id) : Optional.empty();
    }
}
