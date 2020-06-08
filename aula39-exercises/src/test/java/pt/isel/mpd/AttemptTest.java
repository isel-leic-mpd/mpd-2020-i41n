package pt.isel.mpd;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.fail;
import static pt.isel.mpd.App.attempt;

public class AttemptTest {
    @Test
    public void testAttemptOnError() {
        attempt(() -> 3 / 0, (err, val) -> {
            Assert.assertNotNull(err);
            Assert.assertEquals(ArithmeticException.class, err.getClass());
        });
    }
    @Test
    public void testAttemptOnSucceeded() {
        attempt(() -> 128 / 32, (err, val) -> {
            Assert.assertNull(err);
            Assert.assertEquals(4, val.intValue());
        });
    }
}
