package pt.isel.mpd.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractRequest implements Request {
    public abstract InputStream getStream(String path);
    @Override
    final public Iterable<String> getLines(String path) {
        return () -> new Iterator<String>() {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getStream(path)));
            String line;
            boolean called;
            @Override
            public boolean hasNext() {
                if(called) return line != null;
                line = nextLine();
                called = true;
                if(line == null) closeReader();
                return line != null;
            }

            private void closeReader() {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private String nextLine() {
                try {
                    return reader.readLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            public String next() {
                if(!hasNext()) throw new NoSuchElementException();
                called = false;
                return line;
            }
        };
    }
}
