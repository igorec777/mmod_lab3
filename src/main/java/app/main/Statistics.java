package app.main;

import app.state.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;


@Getter
@AllArgsConstructor
public class Statistics {

    public static final int WORK_TIME = 3000;
    public static final double lambda = 0.0333;
    public static final double mu = 0.05;

    public static int appearedReqCount;
    public static int rejectedReqCount;
    public static int servedReqCount;

    public static HashMap<State, Long> statesTime = new HashMap<>();

    public static void ShowStatisticsValues() {
        System.out.println("Total requests generated: " + appearedReqCount);
        System.out.println("Rejected: " + rejectedReqCount);
        System.out.println("Served: " + servedReqCount);
        showTheoreticalValues();
    }

    public static Double[] getStatesTime() {
        String[] statesTitle = {
                "Probability that channel is free: ",
        "Probability that channel is busy: "};
        Double[] result = new Double[State.values().length];
        int i;
        System.out.println("Empirical probabilities:");
        for(Map.Entry<State, Long> entry : statesTime.entrySet()) {
            i = getStateNum(entry);
            result[i] = (entry.getValue() / (double) WORK_TIME);
            System.out.println(statesTitle[i] + result[i]);
        }
        return result;
    }

    public static void showTheoreticalValues() {
        double p0 = finalProbP0();
        double p1 = finalProbP1();
        System.out.println("Theory probabilities:");
        System.out.println("Relative traffic (p0): " + p0);
        System.out.println("Probability of rejection (p1): " + p1);
        System.out.println("Absolute traffic: " + lambda * p0);
    }

    public static Double finalProbP0() {
        return mu / (lambda + mu);
    }

    public static Double finalProbP1() {
        return lambda / (lambda + mu);
    }

    private static int getStateNum(Map.Entry<State, Long> entry) {
        return Integer.parseInt(entry.getKey().toString().substring(1, 2));
    }
}
