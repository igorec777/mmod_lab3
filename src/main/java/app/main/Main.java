package app.main;

import app.mathUtils.Histogram;
import app.mathUtils.Properties;
import app.state.ChannelState;
import config.BeansConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


import java.util.List;

import static app.main.Statistics.*;
import static java.lang.Thread.sleep;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new AnnotationConfigApplicationContext(BeansConfig.class);

        RequestGenerator requestGenerator = context.getBean(RequestGenerator.class);
        ServiceChannel serviceChannel = context.getBean(ServiceChannel.class);

        serviceChannel.start();
        ChannelState.startTime = System.currentTimeMillis();
        requestGenerator.start();

        sleep(WORK_TIME);

        serviceChannel.interrupt();
        requestGenerator.interrupt();

        ShowStatisticsValues();

        new Histogram(new Properties(0,
                "",
                new String[]{"Theoretical probabilities", "Empirical probabilities"},
                List.of(new Double[]{finalProbP0(), finalProbP1()},
                        getStatesTime())));
    }
}
