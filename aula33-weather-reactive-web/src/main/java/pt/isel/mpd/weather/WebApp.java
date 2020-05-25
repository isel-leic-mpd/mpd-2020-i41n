package pt.isel.mpd.weather;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import pt.isel.mpd.util.AsyncHttpRequest;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

import static java.time.LocalDate.of;

public class WebApp {

    static final AsyncWeatherService service = new AsyncWeatherService(new AsyncWeatherRestful(new AsyncHttpRequest()));
    static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);
        app.get("/", ctx -> ctx
            .contentType("text/html")
            .result("<html><h1>Hello World</h1></html>"));
        app.get("/weather/:location", WebApp::pastWeatherHandler);
    }

    private static void pastWeatherHandler(Context context) throws IOException {
        CompletableFuture<Void> cf = new CompletableFuture<>();
        context.result(cf);
        PrintWriter writer = context.res.getWriter();
        pastWeather(context)
            .doOnSubscribe(disp -> context.contentType("text/html"))
            .doOnNext(item -> {
                writer.write(item);
                writer.flush();
            })
            .doOnComplete(() -> cf.complete(null))
            .doOnError(cf::completeExceptionally)
            .subscribe();
    }

    private static void pastWeatherHandlerSync(Context context) throws IOException {
        PrintWriter writer = context.res.getWriter();
        pastWeather(context)
            .doOnSubscribe(disp -> context.contentType("text/html"))
            .doOnNext(item -> {
                writer.write(item);
                writer.flush();
            })
            .doOnComplete(() -> context.result(""))
            .blockingSubscribe();
    }
    private static Observable<String> pastWeather(Context context) throws IOException {
        String location = context.pathParam("location");
        String strTo = context.queryParam("to");
        String strFrom = context.queryParam("from");
        LocalDate to = strTo == null ? LocalDate.now() : LocalDate.parse(strTo, formatter);
        LocalDate from = strFrom == null ? to.minusDays(30) : LocalDate.parse(strFrom, formatter);
        return service
            .search(location)// Observable<Location>
            .firstElement()  // Maybe<Location> <=> Optional + Async <=> CompletableFuture
            .toObservable()  // Observable<Location>
            .flatMap(faro -> faro.pastWeather(from, to)) // Observable<Weather>
            .map(w -> String.format("<br><strong>%s</strong>: %s", w.date, w.desc));
    }
}