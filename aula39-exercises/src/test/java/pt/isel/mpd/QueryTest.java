package pt.isel.mpd;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class QueryTest {
    @Test
    public void testQueryRepeat() {
        Query<Character> actual = Query
            .of('g', 'a', 'p')
            .repeat(4);
        asList('g', 'g', 'g', 'g', 'a', 'a', 'a', 'a', 'p', 'p', 'p', 'p')
            .forEach(expected -> actual.tryAdvance(cur -> assertEquals(expected, cur)));
        assertFalse(actual.tryAdvance(item -> fail("Unexpected more items from repeat()!")));
    }
    @Test
    public void testQueryWindow() {
        Iterator<List<String>> source = Arrays
            .asList(asList("g", "a", "p"), asList("o", "f", "f"), asList("m", "a"))
            .iterator();
        Query
            .of("g", "a", "p", "o", "f", "f", "m", "a")
            .window(3)
            .forEach(sub -> {
                Iterator<String> expected = source.next().iterator();
                sub.forEach(actual -> {
                    System.out.println(actual);
                    assertEquals(expected.next(), actual);
                });
                assertFalse(expected.hasNext());
            });
        assertFalse(source.hasNext());
    }

}
