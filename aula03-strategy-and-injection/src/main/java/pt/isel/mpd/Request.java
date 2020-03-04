package pt.isel.mpd;

import java.util.List;

public interface Request {
    List<String> getLines(String path);
}
