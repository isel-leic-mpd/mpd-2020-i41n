package pt.isel.mpd.weather;

import pt.isel.mpd.util.LazyQueries;
import pt.isel.mpd.weather.dto.LocationInfo;
import pt.isel.mpd.weather.dto.WeatherInfo;
import pt.isel.mpd.weather.model.Location;
import pt.isel.mpd.weather.model.Weather;

import java.time.LocalDate;

import static pt.isel.mpd.util.LazyQueries.map;

public class WeatherService {
    private final WeatherWebApi api;

    public WeatherService(WeatherWebApi api) {
        this.api = api;
    }

    public Iterable<Location> search(String query) {
        Iterable<LocationInfo> locals = api.search(query);
        return map(locals, this::toLocation);
    }

    public Iterable<Weather> pastWeather(double lat, double log, LocalDate from, LocalDate to) {
        Iterable<WeatherInfo> weatherInfos = api.pastWeather(lat, log, from, to);
        return map(weatherInfos, this::toWeather);
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
