package pt.isel.mpd.sub;

import io.reactivex.rxjava3.core.Observable;
import org.reactivestreams.FlowAdapters;
import pt.isel.mpd.pub.TemperaturesPublisher;

import java.util.concurrent.Flow;

public class App {
    public static void main(String[] args) throws InterruptedException {
        TemperaturesPublisher newYork = TemperaturesPublisher.forTown("New York");
        PrinterSubscriber sub = new PrinterSubscriber();
        newYork.subscribe(sub);
        Thread.sleep(3000);
        sub.cancel();

        System.out.println("*** FINISH!!!!");

        Observable
            .fromPublisher(FlowAdapters.toPublisher(newYork))
            .doOnNext(item -> System.out.println(item))
            .take(3)
            .blockingSubscribe();

    }

    static class PrinterSubscriber<T> implements Flow.Subscriber<T> {
        Flow.Subscription subscription;
        private boolean isCanceled;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
        }
        @Override
        public void onNext(T item) {
            System.out.println(item);
            if(!isCanceled)
                subscription.request(1);
        }
        @Override
        public void onError(Throwable throwable) {
            // !!! Not recomended !!!
            throwable.printStackTrace();
        }
        @Override
        public void onComplete() {

        }

        public void cancel() {
            subscription.cancel();
            isCanceled = true;
        }
    }
}
