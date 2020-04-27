package pt.isel.mpd.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class HttpRequest extends AbstractRequest {
    @Override
    public InputStream getStream(String path) {
        try {
            return new URL(path).openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
