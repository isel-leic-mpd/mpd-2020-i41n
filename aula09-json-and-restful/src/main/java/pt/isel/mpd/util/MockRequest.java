package pt.isel.mpd.util;

import java.io.InputStream;

public class MockRequest extends AbstractRequest {
    @Override
    public InputStream getStream(String path) {
        path = path.replace('&', '_')
            .replace(',', '_')
            .replace('=', '_')
            .replace('?', '_')
            .substring(45,113);
        path += ".csv";
        return ClassLoader.getSystemClassLoader().getResourceAsStream(path);
    }
}
