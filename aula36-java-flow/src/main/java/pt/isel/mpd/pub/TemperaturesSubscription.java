package pt.isel.mpd.pub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

public class TemperaturesSubscription implements Flow.Subscription {
    private final Flow.Subscriber subscriber;
    private final String town;
    private final ExecutorService exec = Executors.newSingleThreadExecutor();

    public TemperaturesSubscription(Flow.Subscriber subscriber, String town) {
        this.subscriber = subscriber;
        this.town = town;
    }

    @Override
    public void request(long n) {
        Runnable task = () -> { try{
            for (int i = 0; i < n; i++) {
                TempInfo temp = TempInfo.fetch(town);
                subscriber.onNext(temp);
                sleep(1000);
            }
        } catch(RuntimeException e) {
            subscriber.onError(e);
        }};
        exec.submit(task);
    }

    private static void sleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancel() {
        exec.shutdown();
        subscriber.onComplete();
    }
}

