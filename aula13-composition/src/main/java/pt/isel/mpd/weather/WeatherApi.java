package pt.isel.mpd.weather;

import pt.isel.mpd.weather.dto.LocationInfo;
import pt.isel.mpd.weather.dto.WeatherInfo;

import java.time.LocalDate;

public interface WeatherApi {
    Iterable<WeatherInfo> pastWeather(double lat, double log, LocalDate from, LocalDate to);
    Iterable<LocationInfo> search(String query);
}
