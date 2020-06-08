package pt.isel.mpd;

import java.util.function.Consumer;

public interface Query<T>{
    boolean tryAdvance(Consumer<T> action);

    public static <T> Query<T> of(T...src) {
        int[] idx = {0};
        return cons -> {
            if(idx[0] >= src.length) return false;
            cons.accept(src[idx[0]]);
            return idx[0]++ < src.length;
        };
    }
    public default void forEach(Consumer<T> cons) {
        while(tryAdvance(cons)){}
    }
    public default Query<T> repeat(int mul) {
        throw new UnsupportedOperationException();
    }
    public default Query<Query<T>> window(int size) {
        throw new UnsupportedOperationException();
    }
}