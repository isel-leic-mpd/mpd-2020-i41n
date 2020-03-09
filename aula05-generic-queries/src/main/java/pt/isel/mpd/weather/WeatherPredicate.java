package pt.isel.mpd.weather;

public interface WeatherPredicate {
    boolean test(WeatherInfo info);
}
