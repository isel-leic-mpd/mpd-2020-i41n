package pt.isel.mpd;

import org.junit.Test;
import pt.isel.mpd.util.AsyncMockRequest;
import pt.isel.mpd.util.MockRequest;
import pt.isel.mpd.weather.AsyncWeatherRestful;
import pt.isel.mpd.weather.AsyncWeatherService;
import pt.isel.mpd.weather.WeatherRestful;
import pt.isel.mpd.weather.WeatherService;
import pt.isel.mpd.weather.model.Weather;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.time.LocalDate.of;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WeatherServiceTest {
    @Test public void testPastWeatherOfFaroCity() {
        AsyncWeatherService service = new AsyncWeatherService(new AsyncWeatherRestful(new AsyncMockRequest()));
        CompletableFuture<Stream<Weather>> jan = service
            .search("Faro")                 // CF<Stream<Location>>
            .thenApply(Stream::findFirst)   // CF<Optional<Location>>
            .thenApply(Optional::get)       // CF<Location>
            .thenCompose(faro -> faro.pastWeather(of(2020, 1, 1), of(2020, 1, 30))); // CF<Stream<Wearther>>>
        jan
            .thenApply(strm -> strm.map(Weather::getTempC).max(Integer::compare))
            .thenApply(Optional::get)
            .thenAccept(maxTempJan -> assertEquals(17, maxTempJan.intValue()))
            .join();
    }
}
