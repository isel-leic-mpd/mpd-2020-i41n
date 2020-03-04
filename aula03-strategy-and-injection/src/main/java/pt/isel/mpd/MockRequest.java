package pt.isel.mpd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MockRequest implements Request {
    @Override
    public List<String> getLines(String path) {
        path = path.replace('&', '_')
            .replace(',', '_')
            .replace('=', '_')
            .replace('?', '_')
            .substring(45,113);
        path += ".csv";
        List<String> lines = new ArrayList<>();
        try(
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
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
