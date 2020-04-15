package pt.isel.mpd.util;

import java.util.List;

public interface Request {
    Iterable<String> getLines(String path);
}
