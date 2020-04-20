package pt.isel.mpd.weather;

import pt.isel.mpd.weather.dto.LocationInfo;
import pt.isel.mpd.weather.dto.WeatherInfo;

import java.time.LocalDate;
import java.util.stream.Stream;

public interface WeatherApi {
    Stream<WeatherInfo> pastWeather(double lat, double log, LocalDate from, LocalDate to);
    Stream<LocationInfo> search(String query);
}
