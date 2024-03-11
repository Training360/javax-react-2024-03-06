package employees;

import reactor.core.publisher.Mono;

public class PublisherTypesMain {

    public static void main(String[] args) {
        // Két fajta publisher
        // Hot, azonnal létrehozza az elemet, anélkül hogy feliratkoznának
        // Eager
        // Nem ismételhető
        // Egérmozgatás, http kérések
        // Backpressure (consumer szól, hogy túl sok) kezelhető, technikák: throttling, windows, buffers, and dropping
//        var values = Mono.just(getInitialValue());
//        values.subscribe();
//        values.subscribe();

        // Cold, csak akkor hozza létre az elemet, ha feliratkoznak
        // Http kérés - addig nem hív, míg nem iratkoznak rá fel
        // Lazy

        // just - cold-dá változtatása, defer
        var values = Mono.defer(() -> Mono.just(getInitialValue()));

        values.subscribe();
//        values.subscribe();

        // cold -> hot - share(), reply()
    }

    public static int getInitialValue() {
        System.out.println("getInitialValue");
        return 5;
    }
}
