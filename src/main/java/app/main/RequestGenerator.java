package app.main;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static app.main.DistributeGenerator.generateNextReqTime;

@Component
public class RequestGenerator extends Thread {

    @Autowired
    private ServiceChannel serviceChannel;

    @SneakyThrows
    @Override
    public void run() {

        int totalCount = 1;
        String reqName;

        while (true) {
            reqName = "R" + totalCount;
            Request req = new Request(reqName);
            Statistics.appearedReqCount++;
            try {
                serviceChannel.getPool().add(req);
                System.out.println(reqName + " came");
            }
            catch (IllegalStateException ex) {
                System.out.println(reqName + " rejected");
                Statistics.rejectedReqCount++;
            }
            try {
                sleep(generateNextReqTime());
            }
            catch (InterruptedException ex) {
                return;
            }
            totalCount++;
        }
    }
}
