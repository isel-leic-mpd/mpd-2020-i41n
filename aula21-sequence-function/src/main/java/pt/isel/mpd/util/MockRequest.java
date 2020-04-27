package pt.isel.mpd.util;

import java.io.InputStream;

public class MockRequest extends AbstractRequest {
    @Override
    public InputStream getStream(String path) {
        int end = path.length() > 132 ? 132 : path.length();
        path = path.replace('&', '_')
            .replace(',', '_')
            .replace('=', '_')
            .replace('?', '_')
            .substring(45, end);
        path += ".csv";
        return ClassLoader.getSystemClassLoader().getResourceAsStream(path);
    }
}
