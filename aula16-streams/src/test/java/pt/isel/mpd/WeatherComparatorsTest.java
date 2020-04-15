package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.Comparer;
import pt.isel.mpd.util.MockRequest;
import pt.isel.mpd.weather.WeatherRestful;
import pt.isel.mpd.weather.WeatherService;
import pt.isel.mpd.weather.model.Weather;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.time.LocalDate.of;
import static java.util.Comparator.comparing;
import static org.junit.Assert.assertEquals;

public class WeatherComparatorsTest {
    final WeatherService service = new WeatherService(new WeatherRestful(new MockRequest()));
    final Supplier<Stream<Weather>> jan = () -> service
            .search("Faro")
            .findFirst()
            .get()
            .pastWeather(of(2020, 1, 1), of(2020, 1, 30));
    @Test public void testMaxTemp() {
        Optional<Weather> info = jan.get().max(comparing(Weather::getTempC));
        assertEquals(17, info.get().tempC);
        assertEquals(16, info.get().date.getDayOfMonth());
    }
    @Test public void printWeatherWithMaxtTemp() {
        jan.get().filter(w -> w.getTempC() == 17).forEach(System.out::println);
    }
    @Test public void testMaxTempAndThenByPrecip() {
        Optional<Weather> info = jan.get().max(Comparer.by(Weather::getTempC).thenComparing(Weather::getPrecipMM));
        assertEquals(17, info.get().tempC);
        assertEquals(18, info.get().date.getDayOfMonth());
    }
}
