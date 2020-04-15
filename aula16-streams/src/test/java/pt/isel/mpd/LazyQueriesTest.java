/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.LazyQueries;
import pt.isel.mpd.util.MockRequest;
import pt.isel.mpd.weather.dto.WeatherInfo;
import pt.isel.mpd.weather.WeatherWebApi;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pt.isel.mpd.util.LazyQueries.concat;
import static pt.isel.mpd.util.LazyQueries.distinct;
import static pt.isel.mpd.util.LazyQueries.filter;
import static pt.isel.mpd.util.LazyQueries.generate;
import static pt.isel.mpd.util.LazyQueries.interleave;
import static pt.isel.mpd.util.LazyQueries.limit;
import static pt.isel.mpd.util.LazyQueries.map;
import static pt.isel.mpd.util.LazyQueries.max;

public class LazyQueriesTest {
    final Iterable<WeatherInfo> jan;

    public LazyQueriesTest() {
        WeatherWebApi api = new WeatherWebApi(new MockRequest());
        jan = api.pastWeather(37.017, -7.933, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 30));
    }

    @Test public void testGenerateRandomIntegers() {
        Random rand = new Random();
        Iterator<Integer> nrs = generate(rand::nextInt).iterator();
        System.out.println(nrs.next());
        System.out.println(nrs.next());
        System.out.println(nrs.next());
    }
    int init;
    @Test public void testGenerateEvenIntegers() {
        Iterator<Integer> expected = Arrays.asList(0, 2, 4, 6).iterator();
        init = 0;
        Iterable<Integer> actual = limit(filter(generate(() -> init++), n -> n % 2 == 0), 4);
        actual.forEach(curr -> assertEquals(expected.next(), curr));
        assertFalse(expected.hasNext());
    }

    @Test public void testMaxTemperatureOnJanuary() {
        Optional<Integer> box = max(map(jan, WeatherInfo::getTempC));
        int maxTemp = box.get(); // ASK
        assertEquals(17, maxTemp);
    }

    @Test public void testMaxTemperatureOnSunnyDays() {
        Optional<Integer> box = max(map(filter(jan, wi -> wi.getDesc().contains("Sun")), WeatherInfo::getTempC));
        box  // TELL
            .ifPresentOrElse(
                maxTemp -> assertEquals(15, maxTemp.intValue()),
                () -> assertTrue(false));

    }

    @Test public void testFilterCloudyDays() {
        Iterable<WeatherInfo> cloud = filter(jan, LazyQueriesTest::cloudyDays);
        assertEquals(14, LazyQueries.count(cloud));
    }

    @Test public void filterEvenNumbers() {
        Iterable<Integer> nrs = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        Iterator<Integer> expected = Arrays.asList(2, 4, 6).iterator();
        Iterator<Integer> actual = filter(nrs, n -> n % 2 == 0).iterator();
        while(actual.hasNext()) {
            Integer curr = actual.next();
            assertEquals(expected.next(), curr);
        }
        assertFalse(expected.hasNext());
    }
    @Test public void testDistinctValues(){
        Iterable<Integer> numbers = Arrays.asList(1, 2, 2, 4, 4, 6, 6,0,0,1,2,3,4,5);
        Iterator<Integer> expected = Arrays.asList(1, 2, 4,6,0,3,5).iterator();
        Iterator<Integer> actual = distinct(numbers).iterator();
        while(actual.hasNext()) {
            Integer curr = actual.next();
            assertEquals(expected.next(), curr);
        }
        assertFalse(expected.hasNext());
    }
    @Test
    public void testConcat() {
        Iterable<Integer> iter1 = Arrays.asList(1, 2, 3, 4);
        Iterable<Integer> iter2 = Arrays.asList(5, 6, 7, 8);
        Iterator<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8).iterator();
        Iterator<Integer> actual = concat(iter1, iter2).iterator();
        while(actual.hasNext()) {
            assertEquals(expected.next(), actual.next());
        }
        assertFalse(expected.hasNext());
    }
    @Test public void interleaveNumbers() {
        Iterable<Integer> nrs1 = Arrays.asList(1,2,3);
        Iterable<Integer> nrs2 = Arrays.asList(4,5,6,7,8,9,10);
        Iterator<Integer> expected = Arrays.asList(1,4,2,5,3,6,7,8,9,10).iterator();
        Iterator<Integer> actual = interleave(nrs1,nrs2).iterator();
        while(actual.hasNext()) {
            Integer curr = actual.next();
            assertEquals(expected.next(), curr);
        }
        assertFalse(expected.hasNext());
    }
    @Test public void interleaveNumbersWhithoutCallingHasNext() {
        Iterable<Integer> nrs1 = Arrays.asList(1,2,3);
        Iterable<Integer> nrs2 = Arrays.asList(4,5,6,7,8,9,10);
        Iterator<Integer> expected = Arrays.asList(1,4,2,5,3,6,7,8,9,10).iterator();
        Iterator<Integer> actual = interleave(nrs1,nrs2).iterator();
        while(expected.hasNext()) {
            Integer curr = actual.next();
            assertEquals(expected.next(), curr);
        }
        assertFalse(expected.hasNext());
    }


    static boolean cloudyDays(WeatherInfo info) {
        return info.desc.toLowerCase().contains("cloud");
    }

}
