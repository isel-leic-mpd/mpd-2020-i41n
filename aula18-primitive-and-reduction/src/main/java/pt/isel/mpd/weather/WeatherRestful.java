package pt.isel.mpd.weather;

import com.google.gson.Gson;
import pt.isel.mpd.util.Request;
import pt.isel.mpd.weather.dto.LocationInfo;
import pt.isel.mpd.weather.dto.PastWeatherDataWeatherDto;
import pt.isel.mpd.weather.dto.PastWeatherDto;
import pt.isel.mpd.weather.dto.SearchApiResultDto;
import pt.isel.mpd.weather.dto.SearchDto;
import pt.isel.mpd.weather.dto.WeatherInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.stream.Stream;

public class WeatherRestful implements  WeatherApi  {
    final static String HOST = "http://api.worldweatheronline.com/premium/v1/";
    final static String PATH_PAST_WEATHER = "past-weather.ashx?q=%s,%s&date=%s&enddate=%s&tp=24&format=json&key=%s";
    final static String PATH_SEARCH = "search.ashx?query=%s&format=json&key=%s";
    final static String WEATHER_KEY;

    private final Request req;
    private final Gson gson = new Gson();

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

    public WeatherRestful(Request req) {
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
    public Stream<WeatherInfo> pastWeather(double lat, double log, LocalDate from, LocalDate to) {
        String path = HOST + String.format(PATH_PAST_WEATHER, lat, log, from, to, WEATHER_KEY);
        Iterable<String> lines = req.getLines(path);
        String body = String.join("",lines);
        PastWeatherDto dto = gson.fromJson(body, PastWeatherDto.class);
        PastWeatherDataWeatherDto[] infos = dto.getData().getWeather();
        return Stream.of(infos).map(WeatherRestful::toPastWeather);
    }
    private static WeatherInfo toPastWeather(PastWeatherDataWeatherDto dto){
        return new WeatherInfo(
                LocalDate.parse(dto.getDate()),
                dto.getHourly()[0].getTempC(),
                dto.getHourly()[0].getPrecipMM(),
                dto.getHourly()[0].getWeatherDesc()
        );
    }
    /**
     * e.g. http://api.worldweatheronline.com/premium/v1/search.ashx?query=Oporto&format=tab&key=10a7e54b547c4c7c870162131192102
     *
     * @param query Name of the city you are looking for.
     * @return List of LocationInfo objects with location information.
     */
    public Stream<LocationInfo> search(String query) {
        String path = HOST + String.format(PATH_SEARCH, query, WEATHER_KEY);
        Iterable<String> lines = req.getLines(path);
        String body = String.join("", lines);
        SearchDto dto = gson.fromJson(body, SearchDto.class);
        return Stream
            .of(dto.getSearch_api().getResult())
            .map(WeatherRestful::toLocationInfo);
    }

    private static LocationInfo toLocationInfo(SearchApiResultDto dto) {
        return new LocationInfo(
            dto.getCountry()[0].getValue(),
            dto.getRegion()[0].getValue(),
            Double.parseDouble(dto.getLatitude()),
            Double.parseDouble(dto.getLongitude()));
    }
}
