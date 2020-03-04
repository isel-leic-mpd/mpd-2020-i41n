package pt.isel.mpd.weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherQueries {

    /**
     * Version 1
     */
    public static Iterable<WeatherInfo> filterSunnyDays(Iterable<WeatherInfo> src) {
        List<WeatherInfo> res = new ArrayList<>();
        for (WeatherInfo info: src) {
            if(info.desc.toLowerCase().contains("sun"))
                res.add(info);
        }
        return res;
    }
    public static Iterable<WeatherInfo> filterCloudyDays(Iterable<WeatherInfo> src) {
        List<WeatherInfo> res = new ArrayList<>();
        for (WeatherInfo info: src) {
            if(info.desc.toLowerCase().contains("cloud"))
                res.add(info);
        }
        return res;
    }

    /**
     * Versao 2
     */
    public static Iterable<WeatherInfo> filterByDesc(Iterable<WeatherInfo> src, String desc) {
        List<WeatherInfo> res = new ArrayList<>();
        for (WeatherInfo info: src) {
            if(info.desc.toLowerCase().contains(desc))
                res.add(info);
        }
        return res;
    }
    /**
     * Versao 3
     */
    public static Iterable<WeatherInfo> filter(Iterable<WeatherInfo> src, WeatherPredicate pred) {
        List<WeatherInfo> res = new ArrayList<>();
        for (WeatherInfo info: src) {
            if(pred.test(info))
                res.add(info);
        }
        return res;
    }
}
