package pt.isel.mpd.util;

import java.io.InputStream;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractRequest implements Request {
    public abstract InputStream getStream(String path);
    @Override
    final public Iterable<String> getLines(String path) {
        return () -> new IteratorInputStream(getStream(path));
    }

    @Override
    final public Stream<String> stream(String path) {
        Supplier<InputStream> sup = () -> getStream(path);
        return StreamSupport.stream(new SpliteratorInputStream(sup), false);
    }

}
