package pt.isel.mpd.weather;

@FunctionalInterface
public interface WeatherPredicate {
    boolean test(WeatherInfo info);
}
