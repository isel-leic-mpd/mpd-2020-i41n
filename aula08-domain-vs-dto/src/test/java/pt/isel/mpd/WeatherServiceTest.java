package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.HttpRequest;
import pt.isel.mpd.util.LazyQueries;
import pt.isel.mpd.weather.WeatherService;
import pt.isel.mpd.weather.WeatherWebApi;
import pt.isel.mpd.weather.model.Weather;

import java.time.LocalDate;

import static java.lang.System.out;
import static java.time.LocalDate.of;
import static pt.isel.mpd.util.LazyQueries.first;

public class WeatherServiceTest {
    @Test public void testPastWeatherOfFaroCity() {
        WeatherService service = new WeatherService(new WeatherWebApi(new HttpRequest()));
        first(service.search("Faro"))
            .pastWeather(of(2020, 1, 1), of(2020, 1, 30))
            .forEach(out::println);
    }
}
