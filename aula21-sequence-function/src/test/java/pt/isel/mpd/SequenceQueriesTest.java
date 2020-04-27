package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.LazyQueries;
import pt.isel.mpd.util.Sequence;
import pt.isel.mpd.util.SequenceQueries;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static pt.isel.mpd.util.SequenceQueries.forEach;
import static pt.isel.mpd.util.SequenceQueries.of;
import static pt.isel.mpd.util.SequenceQueries.filter;
import static pt.isel.mpd.util.SequenceQueries.limit;
import static pt.isel.mpd.util.SequenceQueries.generate;

public class SequenceQueriesTest {

    @Test
    public void testGenerateRandomIntegers() {
        Random rand = new Random();
        Sequence<Integer> nrs = generate(rand::nextInt);
        nrs.tryAdvance(System.out::println);
        nrs.tryAdvance(System.out::println);
        nrs.tryAdvance(System.out::println);
    }

    int init;
    @Test public void testGenerateEvenIntegers() {
        Sequence<Integer> expected = of(0, 2, 4, 6);
        init = 0;
        Sequence<Integer> actual = limit(filter(generate(() -> init++), n -> n % 2 == 0), 4);
        forEach(actual, curr -> expected.tryAdvance(other -> assertEquals(other, curr)));
        assertFalse(expected.tryAdvance(__ -> {}));
        assertFalse(actual.tryAdvance(__ -> {}));
    }

}
