package pt.isel.mpd.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface Sequence<T> {
    boolean tryAdvance(Consumer<T> action);

    // Métodos Factory

    static <T> Sequence<T> generate(Supplier<T> sup) {
        /**
         * Implementation of tryAdvance(Consumer) method.
         */
        return cons -> {
            cons.accept(sup.get());
            return true;
        };
    }

    static <T> Sequence<T> of(T...args) {
        BoxInt index = new BoxInt();
        return cons -> {
            if(index.val >= args.length) return false;
            cons.accept(args[index.val++]);
            return true;
        };
    }
    // Operações intermédias
    default Sequence<T> limit(int size) {
        BoxInt index = new BoxInt();
        return cons ->  index.val++ >= size
            ? false
            : this.tryAdvance(cons);
    }
    default Sequence<T> filter(Predicate<T> pred) {
        return cons -> {
            BoxBool found = new BoxBool();
            while(!found.val && this.tryAdvance(item -> {
                if(pred.test(item)) {
                    found.val = true;
                    cons.accept(item);
                }
            })) { }
            return found.val;
        };
    }
    default <R> Sequence<R> map(Function<T, R> mapper) {
        return cons -> tryAdvance(item -> cons.accept(mapper.apply(item)));
    }
    // Operações Terminais
    default void forEach(Consumer<T> cons) {
        while(this.tryAdvance(cons)) {}
    }


    static class BoxInt { int val; }
    static class BoxBool { boolean val; }

}
