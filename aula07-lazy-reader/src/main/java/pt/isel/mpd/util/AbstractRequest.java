package pt.isel.mpd.util;

import java.io.InputStream;

public abstract class AbstractRequest implements Request {
    public abstract InputStream getStream(String path);
    @Override
    final public Iterable<String> getLines(String path) {
        return () -> new IteratorInputStream(getStream(path));
    }
}
