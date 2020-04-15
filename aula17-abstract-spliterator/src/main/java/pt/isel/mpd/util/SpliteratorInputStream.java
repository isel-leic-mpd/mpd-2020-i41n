package pt.isel.mpd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SpliteratorInputStream extends Spliterators.AbstractSpliterator {
    final Supplier<InputStream> sup;
    BufferedReader reader;

    protected SpliteratorInputStream(Supplier<InputStream> sup) {
        super(Long.MAX_VALUE, Spliterator.ORDERED);
        this.sup = sup;
    }
    BufferedReader getReader() {
        if(reader == null) {
            reader = new BufferedReader(new InputStreamReader(sup.get()));
        }
        return reader;
    }
    @Override
    public boolean tryAdvance(Consumer action) {
        String line = readLine();
        if(line == null)  return false;
        else action.accept(line);
        return true;
    }

    private String readLine() {
        try {
            return getReader().readLine();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
