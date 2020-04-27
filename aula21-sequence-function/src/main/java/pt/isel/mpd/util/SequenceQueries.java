package pt.isel.mpd.util;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class SequenceQueries {

    // Métodos Factory

    public static <T> Sequence<T> generate(Supplier<T> sup) {
        /**
         * Implementation of tryAdvance(Consumer) method.
         */
        return cons -> {
            cons.accept(sup.get());
            return true;
        };
    }

    public static <T> Sequence<T> of(T...args) {
        BoxInt index = new BoxInt();
        return cons -> {
            if(index.val >= args.length) return false;
            cons.accept(args[index.val++]);
            return true;
        };
    }
    // Operações intermédias
    public static <T> Sequence<T> limit(Sequence<T> src, int size) {
        BoxInt index = new BoxInt();
        return cons ->  index.val++ >= size
            ? false
            : src.tryAdvance(cons);
    }
    public static <T> Sequence<T> filter(Sequence<T> src, Predicate<T> pred) {
        return cons -> {
            BoxBool found = new BoxBool();
            while(!found.val && src.tryAdvance(item -> {
                if(pred.test(item)) {
                    found.val = true;
                    cons.accept(item);
                }
            })) { }
            return found.val;
        };
    }

    // Operações Terminais
    public static <T> void forEach(Sequence<T> src, Consumer<T> cons) {
        while(src.tryAdvance(cons)) {}
    }


    static class BoxInt { int val; }
    static class BoxBool { boolean val; }
}
