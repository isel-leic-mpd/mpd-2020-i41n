package pt.isel.mpd;

import org.junit.Assert;
import org.junit.Test;
import pt.isel.mpd.util.HttpRequest;
import pt.isel.mpd.util.LazyQueries;
import pt.isel.mpd.util.MockRequest;
import pt.isel.mpd.weather.WeatherRestful;
import pt.isel.mpd.weather.WeatherService;
import pt.isel.mpd.weather.WeatherWebApi;
import pt.isel.mpd.weather.model.Weather;

import java.time.LocalDate;

import static java.lang.System.out;
import static java.time.LocalDate.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pt.isel.mpd.util.LazyQueries.first;
import static pt.isel.mpd.util.LazyQueries.map;
import static pt.isel.mpd.util.LazyQueries.max;

public class WeatherServiceTest {
    @Test public void testPastWeatherOfFaroCity() {
        WeatherService service = new WeatherService(new WeatherRestful(new MockRequest()));
        Iterable<Weather> jan = first(service.search("Faro"))
            .pastWeather(of(2020, 1, 1), of(2020, 1, 30));
        max(map(jan, Weather::getTempC))
            .ifPresentOrElse(
                maxTempJan -> assertEquals(17, maxTempJan.intValue()),
                () -> assertTrue(false));

    }
}
