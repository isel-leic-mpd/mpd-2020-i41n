package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.Sequence;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static pt.isel.mpd.util.Sequence.generate;
import static pt.isel.mpd.util.Sequence.of;

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
        Sequence<String> expected = of("0", "2", "4", "6");
        init = 0;
        Sequence
            .generate(() -> init++)
            .filter(n -> n % 2 == 0)
            .limit(4)
            .map(String::valueOf)
            .forEach(curr -> expected.tryAdvance(other -> assertEquals(other, curr)));
        assertFalse(expected.tryAdvance(__ -> {}));
    }

}
