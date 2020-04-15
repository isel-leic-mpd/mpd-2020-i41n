package pt.isel.mpd.util;

import java.util.List;
import java.util.stream.Stream;

public interface Request {
    Iterable<String> getLines(String path);

    Stream<String> stream(String path);
}
