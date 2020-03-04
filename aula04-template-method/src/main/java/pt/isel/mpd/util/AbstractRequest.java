package pt.isel.mpd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRequest implements Request {
    abstract InputStream getStream(String path);
    @Override
    final public List<String> getLines(String path) {
        List<String> lines = new ArrayList<>();
        try(
            InputStream in = getStream(path);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
}
