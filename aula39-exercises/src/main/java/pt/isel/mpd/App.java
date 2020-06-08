package pt.isel.mpd;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
    }

    public static <T> void attempt(Supplier<T> sup, BiConsumer<Exception, T> cb) {
        throw new UnsupportedOperationException();
    }

    static <T> CompletableFuture<Boolean> all(Stream<CompletableFuture<T>> cfs, Predicate<T> pred) {
        throw new UnsupportedOperationException();
    }

    static <T> CompletableFuture<T> first(Stream<CompletableFuture<T>> cfs, Predicate<T> pred) {
        throw new UnsupportedOperationException();
    }

}
