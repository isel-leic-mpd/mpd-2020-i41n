package pt.isel.mpd.pub;

import java.util.concurrent.Flow;

public class TemperaturesPublisher implements Flow.Publisher {
    private final String town;

    public TemperaturesPublisher(String town) {
        this.town = town;
    }

    public static TemperaturesPublisher forTown(String town) {
        return new TemperaturesPublisher(town);
    }

    @Override
    public void subscribe(Flow.Subscriber subscriber) {
        TemperaturesSubscription sign = new TemperaturesSubscription(subscriber, town);
        subscriber.onSubscribe(sign);
    }
}
