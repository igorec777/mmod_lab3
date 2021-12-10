package app.main;

import java.util.concurrent.ThreadLocalRandom;

import static app.main.Statistics.lambda;


public class DistributeGenerator {

    public static long generateNextReqTime() {
        double time = exponentialDistribution(lambda);
        return (long) time;
    }

    private static double exponentialDistribution(double param) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        return Math.log(1 - rand.nextDouble())/(-param);
    }

}
