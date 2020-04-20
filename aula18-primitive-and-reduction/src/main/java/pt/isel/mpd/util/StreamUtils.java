package pt.isel.mpd.util;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    /**
     * Returns a new Stream that collapses adjacent elements.
     * e.g. src = 2, 1, 1, 3, 2, 2, 1, 5
     *  -> 2, 1, 3, 2, 1, 5
     */
    public static <T> Stream<T> collapse(Stream<T> src) {
        Spliterator<T> res = new CollapseSpliterator(src);
        return StreamSupport.stream(res, false);
    }
}
