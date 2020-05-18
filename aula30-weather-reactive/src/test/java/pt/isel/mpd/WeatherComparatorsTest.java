package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.AsyncMockRequest;
import pt.isel.mpd.util.Comparer;
import pt.isel.mpd.weather.AsyncWeatherRestful;
import pt.isel.mpd.weather.AsyncWeatherService;
import pt.isel.mpd.weather.model.Weather;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.time.LocalDate.of;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class WeatherComparatorsTest {
    final AsyncWeatherService service = new AsyncWeatherService(new AsyncWeatherRestful((new AsyncMockRequest())));
    /**
     * This pipeline wastes the advantage of asynchronous CF and is blocking on join().
     * Only valid for unit testing purposes.
     */
    final Supplier<Stream<Weather>> jan = () -> service
            .search("Faro")
            .thenCompose(faro -> faro
                .findFirst()
                .get()
                .pastWeather(of(2020, 1, 1), of(2020, 1, 30)))
                .join();

    @Test public void testMaxTemp() {
        Optional<Weather> info = jan
            .get()
            .max(comparing(Weather::getTempC));
        assertEquals(17, info.get().tempC);
        assertEquals(16, info.get().date.getDayOfMonth());
    }
    @Test public void testMaxTemp2() {
        /*
        Optional<Integer> max = jan
            .get()                  // Stream<Weather>
            .map(Weather::getTempC) // Stream<Integer> => !!!! boxing OVERHEAD
            .max(Integer::compare); // Optional<Integer> => !!! unboxing Overhead
         */
        OptionalInt max = jan
            .get()                       // Stream<Weather>
            .mapToInt(Weather::getTempC) // IntStream
            .max();

        assertEquals(17, max.getAsInt());
    }
    @Test public void testSumTemp() {
        int sum = jan
            .get()                       // Stream<Weather>
            .mapToInt(Weather::getTempC) // IntStream
            .sum();
        int actual = jan
            .get()                       // Stream<Weather>
            .mapToInt(Weather::getTempC) // IntStream
            .reduce(0, (prev, curr) -> prev + curr);
        assertEquals(sum, actual);
    }
    @Test public void testSumTemp2() {
        int sum = jan.get().mapToInt(Weather::getTempC).sum();
        Integer actual = jan
            .get()         // Stream<Weather>
            .parallel()
            .reduce(
                0,
                (prev, curr) -> prev + curr.getTempC(), // accumulator
                (sum1, sum2) -> sum1 + sum2);// combiner
        assertEquals(sum, actual.intValue());
    }
    @Test public void testAddIntsToListViaReduce() {
        Random rand = new Random();
        /**
         * !!!! Dont't do This
         * => Mutable Reduction should user collect() instead!!!
         */
        LinkedList<Integer> actual = Stream
            .generate(() -> rand.nextInt(100))
            .limit(1024 * 8)
            .parallel()
            .reduce(
                new LinkedList<>(),
                (lst, curr) -> {
                    lst.add(curr);
                    return lst;
                },
                (l1, l2) -> {
                    l1.addAll(l2);
                    return l1;
                }
            );
        assertNotEquals(1024 * 8, actual.size());
    }
    @Test public void testAddIntsToListViaCollect() {
        Random rand = new Random();
        LinkedList<Integer> actual = Stream
            .generate(() -> rand.nextInt(100))
            .limit(1024 * 8)
            .parallel()
            .collect(
                LinkedList::new,
                LinkedList::add,
                LinkedList::addAll);
        assertEquals(1024 * 8, actual.size());
    }
    @Test public void testAddIntsToList() {
        Random rand = new Random();
        List<Integer> actual = Stream
            .generate(() -> rand.nextInt(100))
            .limit(1024 * 8)
            .parallel()
            .collect(toList());
        assertEquals(1024 * 8, actual.size());
    }


    @Test public void printWeatherWithMaxtTemp() {
        jan.get().filter(w -> w.getTempC() == 17).forEach(System.out::println);
    }
    @Test public void testMaxTempAndThenByPrecip() {
        Optional<Weather> info = jan.get().max(Comparer.by(Weather::getTempC).thenComparing(Weather::getPrecipMM));
        assertEquals(17, info.get().tempC);
        assertEquals(18, info.get().date.getDayOfMonth());
    }
}
