package app.main;

import app.state.ChannelState;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static app.main.Statistics.mu;
import static app.main.Statistics.statesTime;


@Component
@Getter
public class ServiceChannel extends Thread {

    private final BlockingQueue<Request> pool = new LinkedBlockingQueue<>(1);

    @SneakyThrows
    @Override
    public void run() {

        Request currReq;
        long newTime;

        while (true) {
            try {
                currReq = pool.element();
                if (currReq != null) {

                    newTime = System.currentTimeMillis();
                    statesTime.merge(
                            app.state.State.S0,
                            newTime - ChannelState.startTime,
                            Long::sum
                    );
                    ChannelState.startTime = newTime;

                    Statistics.servedReqCount++;
                    System.out.println(Thread.currentThread().getName() + " take " + currReq.getReqName());
                    sleep((long)(1.0 / mu));

                    newTime = System.currentTimeMillis();
                    statesTime.merge(
                            app.state.State.S1,
                            newTime - ChannelState.startTime,
                            Long::sum
                    );
                    ChannelState.startTime = newTime;

                    System.out.println(Thread.currentThread().getName() + " processed " + currReq.getReqName());
                    pool.poll();
                }
            }
            catch (NoSuchElementException ignored) {
            }
            catch (InterruptedException ex) {
                return;
            }
        }
    }
}
