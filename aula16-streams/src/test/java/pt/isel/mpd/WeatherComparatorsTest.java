package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.Cmp;
import pt.isel.mpd.util.Comparer;
import pt.isel.mpd.util.LazyQueries;
import pt.isel.mpd.util.MockRequest;
import pt.isel.mpd.weather.WeatherRestful;
import pt.isel.mpd.weather.WeatherService;
import pt.isel.mpd.weather.model.Weather;

import java.util.Optional;

import static java.time.LocalDate.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static pt.isel.mpd.util.Cmp.by;
import static pt.isel.mpd.util.LazyQueries.filter;
import static pt.isel.mpd.util.LazyQueries.first;
import static pt.isel.mpd.util.LazyQueries.map;
import static pt.isel.mpd.util.LazyQueries.max;

public class WeatherComparatorsTest {
    final WeatherService service = new WeatherService(new WeatherRestful(new MockRequest()));
    final Iterable<Weather> jan = first(service.search("Faro"))
            .pastWeather(of(2020, 1, 1), of(2020, 1, 30));

    @Test public void testMaxTemp() {
        /**
         * A expressão lambda representa uma operação aritmética não
         * tornando evidente que se trata de um critério de ordenação/comparação.
         */
        // Optional<Weather> info = max(jan, (w1, w2) -> w1.getTempC() - w2.getTempC());
        // Optional<Weather> info = max(jan, new Cmp<>(Weather::getTempC));
        Optional<Weather> info = max(jan, by(Weather::getTempC));
        assertEquals(17, info.get().tempC);
        assertEquals(16, info.get().date.getDayOfMonth());
    }

    @Test public void printWeatherWithMaxtTemp() {
        filter(jan, w -> w.getTempC() == 17).forEach(System.out::println);
    }
    @Test public void testMaxTempAndThenByPrecip() {
        /**
         * Abordagem 1: Correcto mas obriga a modificar a API existente e sua implementação
         */
        // Optional<Weather> info = max(jan, by(Weather::getTempC, Weather::getPrecipMM));
        /**
         * Abordagem 2
         */
        // Optional<Weather> info = max(jan, by(Weather::getTempC).andThen(Weather::getPrecipMM));
        Optional<Weather> info = max(jan, Comparer.by(Weather::getTempC).andThen(Weather::getPrecipMM));
        assertEquals(17, info.get().tempC);
        assertEquals(18, info.get().date.getDayOfMonth());
    }
}
