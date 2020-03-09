/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package pt.isel.mpd;

import org.junit.Assert;
import org.junit.Test;
import pt.isel.mpd.util.MockRequest;
import pt.isel.mpd.weather.WeatherInfo;
import pt.isel.mpd.weather.WeatherWebApi;

import java.time.LocalDate;
import java.util.List;

public class WeatherWebApiTest {
    @Test public void testPastWeather() {
        WeatherWebApi api = new WeatherWebApi(new MockRequest());
        List<WeatherInfo> jan = api.pastWeather(37.017, -7.933, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 30));
        WeatherInfo first = jan.iterator().next();
        Assert.assertEquals(14, first.tempC);
    }

    // TPC: Add unit test for search() of WeatherWebApi
}
