package pt.isel.mpd.weather;

import pt.isel.mpd.weather.dto.LocationInfo;
import pt.isel.mpd.weather.dto.WeatherInfo;
import pt.isel.mpd.weather.model.Location;
import pt.isel.mpd.weather.model.Weather;

import java.time.LocalDate;
import java.util.stream.Stream;

public class WeatherService {
    private final WeatherApi api;

    public WeatherService(WeatherApi api) {
        this.api = api;
    }

    public Stream<Location> search(String query) {
        return api
            .search(query)
            .map(this::toLocation);
    }

    public Stream<Weather> pastWeather(double lat, double log, LocalDate from, LocalDate to) {
        return api
            .pastWeather(lat, log, from, to)
            .map(this::toWeather);
    }

    private Location toLocation(LocationInfo dto) {
        return new Location(
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
