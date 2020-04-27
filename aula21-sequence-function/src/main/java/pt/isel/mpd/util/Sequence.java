package pt.isel.mpd.util;

import java.util.function.Consumer;

public interface Sequence<T> {
    boolean tryAdvance(Consumer<T> action);
}
