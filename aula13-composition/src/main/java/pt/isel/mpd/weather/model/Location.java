package pt.isel.mpd.weather.model;

import java.time.LocalDate;
import java.util.function.BiFunction;

public class Location {
    private final String country;
    private final String region;
    private final double latitude;
    private final double longitude;
    private final BiFunction<LocalDate, LocalDate, Iterable<Weather>> pastGetter;

    public Location(
        String country,
        String region,
        double latitude,
        double longitude,
        BiFunction<LocalDate, LocalDate, Iterable<Weather>> pastGetter)
     {
        this.country = country;
        this.region = region;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pastGetter = pastGetter;
    }

    public Iterable<Weather> pastWeather(LocalDate from, LocalDate to) {
        return pastGetter.apply(from, to);
    }

    @Override
    public String toString() {
        return "LocationInfo{" +
            "country='" + country + '\'' +
            ", region='" + region + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            '}';
    }
}
