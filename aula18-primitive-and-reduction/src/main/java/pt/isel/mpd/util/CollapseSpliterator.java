package pt.isel.mpd.util;

import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CollapseSpliterator<T> extends Spliterators.AbstractSpliterator<T> {
    public CollapseSpliterator(Stream<T> src) {
        super(src.spliterator().estimateSize(), src.spliterator().characteristics());
    }
    /**
     * Collapses adjacent elements from src.
     * e.g. src = 2, 1, 1, 3, 2, 2, 1, 5
     *  -> 2, 1, 3, 2, 1, 5
     */
    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return false;
    }
}
