package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.MockRequest;
import pt.isel.mpd.weather.WeatherRestful;
import pt.isel.mpd.weather.WeatherService;
import pt.isel.mpd.weather.model.Weather;

import java.util.stream.Stream;

import static java.time.LocalDate.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WeatherServiceTest {
    @Test public void testPastWeatherOfFaroCity() {
        WeatherService service = new WeatherService(new WeatherRestful(new MockRequest()));
        Stream<Weather> jan = service
            .search("Faro")
            .findFirst()
            .get()
            .pastWeather(of(2020, 1, 1), of(2020, 1, 30));
        jan
            .map(Weather::getTempC)
            .max(Integer::compare)
            .ifPresentOrElse(
                maxTempJan -> assertEquals(17, maxTempJan.intValue()),
                () -> assertTrue(false));

    }
}
