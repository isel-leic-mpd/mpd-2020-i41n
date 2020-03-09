package pt.isel.mpd.weather;

import pt.isel.mpd.util.Request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WeatherWebApi {
    final static String HOST = "http://api.worldweatheronline.com/premium/v1/";
    final static String PATH_PAST_WEATHER = "past-weather.ashx?q=%s,%s&date=%s&enddate=%s&tp=24&format=csv&key=%s";
    final static String PATH_SEARCH = "search.ashx?query=%s&format=tab&key=%s";
    final static String WEATHER_KEY;

    private final Request req;

    static {
        try(
            InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("WEATHER_KEY.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in)))
        {
            WEATHER_KEY = reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading WEATHER_KEY.txt. Put your world weather online key within WEATHER_KEY.txt in resources folder.");
        }
    }

    public WeatherWebApi(Request req) {
        this.req = req;
    }

    /**
     * E.g. http://api.worldweatheronline.com/premium/v1/past-weather.ashx?q=37.017,-7.933&date=2019-01-01&enddate=2019-01-30&tp=24&format=csv&key=10a7e54b547c4c7c870162131192102
     *
     * @param lat Location latitude
     * @param log Location longitude
     * @param from Beginning date
     * @param to End date
     * @return List of WeatherInfo objects with weather information.
     */
    public List<WeatherInfo> pastWeather(double lat, double log, LocalDate from, LocalDate to) {
        String path = HOST + String.format(PATH_PAST_WEATHER, lat, log, from, to, WEATHER_KEY);
        List<WeatherInfo> infos = new ArrayList<>();
        List<String> body = req.getLines(path);
        Iterator<String> reader = body.iterator();
        while (reader.next().startsWith("#")) { } // Skip comments
        reader.next();                            // Skip Not Available
        while (reader.hasNext()) {
            infos.add(WeatherInfo.valueOf(reader.next()));
            if(reader.hasNext()) reader.next();                        // Skip daily information
        }
        return infos;
    }

    /**
     * e.g. http://api.worldweatheronline.com/premium/v1/search.ashx?query=Oporto&format=tab&key=10a7e54b547c4c7c870162131192102
     *
     * @param query Name of the city you are looking for.
     * @return List of LocationInfo objects with location information.
     */
    public List<LocationInfo> search(String query) {
        String path = HOST + String.format(PATH_SEARCH, query, WEATHER_KEY);
        System.out.println(path);
        List<LocationInfo> locations = new ArrayList<>();
        List<String> body = req.getLines(path);
        Iterator<String> reader = body.iterator();
        while (reader.hasNext()) {
            String line = reader.next();
            if(line.startsWith("#")) continue;
            locations.add(LocationInfo.valueOf(line));
        }
        return locations;
    }
}
