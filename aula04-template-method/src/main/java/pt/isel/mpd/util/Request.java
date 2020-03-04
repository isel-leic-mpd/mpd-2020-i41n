package pt.isel.mpd.util;

import java.util.List;

public interface Request {
    List<String> getLines(String path);
}
