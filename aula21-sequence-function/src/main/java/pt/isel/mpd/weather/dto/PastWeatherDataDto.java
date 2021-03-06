package pt.isel.mpd.weather.dto;

public class PastWeatherDataDto {
    private final PastWeatherDataWeatherDto[] weather;

    public PastWeatherDataDto(PastWeatherDataWeatherDto[] weather) {
        this.weather = weather;
    }

    public PastWeatherDataWeatherDto[] getWeather() {
        return weather;
    }
}
