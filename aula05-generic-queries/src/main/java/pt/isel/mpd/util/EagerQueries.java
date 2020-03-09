package pt.isel.mpd.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class EagerQueries {
     /**
     * Versao 4
     */
    public static <T> Iterable<T> filter(Iterable<T> src, Predicate<T> pred) {
        List<T> res = new ArrayList<>();
        for (T info: src) {
            if(pred.test(info))
                res.add(info);
        }
        return res;
    }
    public static <T> Iterable<T> skip(Iterable<T> src, int nr) {
        Iterator<T> iter = src.iterator();
        while(nr-- > 0 && iter.hasNext()) iter.next();
        List<T> res = new ArrayList<>();
        while(iter.hasNext()) res.add(iter.next());
        return res;
    }
    public static <T, R> Iterable<R> map(Iterable<T> src, Function<T, R> mapper) {
        List<R> res = new ArrayList<>();
        for (T item: src) {
            res.add(mapper.apply(item));
        }
        return res;
    }

    /**
     * Terminal operation
     */
    public static <T> int count(Iterable<T> src) {
        int nr = 0;
        for (T item: src) { nr++; }
        return nr;
    }
    /**
     * Terminal operation
     */
    public static <T extends Comparable<T>> T max(Iterable<T> src) {
        Iterator<T> iter = src.iterator();
        if(!iter.hasNext()) throw new IllegalArgumentException("Source sequence is empty!");
        T first = iter.next();
        while(iter.hasNext()) {
            T curr = iter.next();
            if(curr.compareTo(first) > 0)
                first = curr;
        }
        return first;
    }


}
