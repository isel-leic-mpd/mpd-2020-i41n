package pt.isel.mpd.weather;

import pt.isel.mpd.weather.dto.LocationInfo;
import pt.isel.mpd.weather.dto.WeatherInfo;
import pt.isel.mpd.weather.model.AsyncLocation;
import pt.isel.mpd.weather.model.Weather;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public class AsyncWeatherService {
    private final AsyncWeatherApi api;

    public AsyncWeatherService(AsyncWeatherApi api) {
        this.api = api;
    }

    public CompletableFuture<Stream<AsyncLocation>> search(String query) {
        return api
            .search(query) // CF<List<LocationInfo>>
            .thenApply(List::stream)
            .thenApply(strm -> strm.map(this::toLocation));
    }

    public CompletableFuture<Stream<Weather>> pastWeather(double lat, double log, LocalDate from, LocalDate to) {
        return api
            .pastWeather(lat, log, from, to)
            .thenApply(List::stream)
            .thenApply(strm -> strm.map(this::toWeather));
    }

    private AsyncLocation toLocation(LocationInfo dto) {
        return new AsyncLocation(
            dto.country,
            dto.region,
            dto.latitude,
            dto.longitude,
            (from, to) -> this.pastWeather(dto.latitude, dto.longitude, from, to));
    }

    private Weather toWeather(WeatherInfo dto) {
        return new Weather(dto.date, dto.tempC, dto.precipMM, dto.desc);
    }

}
