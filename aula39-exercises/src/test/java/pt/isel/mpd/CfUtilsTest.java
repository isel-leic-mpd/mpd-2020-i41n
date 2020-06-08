package pt.isel.mpd;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static pt.isel.mpd.App.attempt;

public class CfUtilsTest {
    @Test
    public void testAllTrue() {
        List<CompletableFuture<String>> cfs = asList(
            completedFuture("ab"), completedFuture("isel"), completedFuture("dive"));
        Boolean actual = App
            .all(cfs.stream(), word -> word.length() % 2 == 0)
            .join();
        Assert.assertTrue(actual);
    }
    @Test
    public void testAllOneFalse() {
        List<CompletableFuture<String>> cfs = asList(
            completedFuture("ab"), completedFuture("super"), completedFuture("dive"));
        Boolean actual = App
            .all(cfs.stream(), word -> word.length() % 2 == 0)
            .join();
        Assert.assertFalse(actual);
    }
    @Test
    public void testFirst() {
        List<CompletableFuture<String>> cfs = asList(
            completedFuture("ab"), completedFuture("super"), completedFuture("dive"));
        String actual = App
            .first(cfs.stream(), word -> word.length() % 2 != 0)
            .join();
        Assert.assertEquals("super", actual);
    }
}
