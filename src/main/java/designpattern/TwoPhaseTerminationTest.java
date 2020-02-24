package designpattern;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Zsyu
 * @Date: 20-2-24 下午4:02
 */
@Slf4j(topic = "main")
public class TwoPhaseTerminationTest {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination twoPhaseTermination = new TwoPhaseTermination();
        twoPhaseTermination.start();
        Thread.sleep(5000);
        twoPhaseTermination.stop();

    }
}
@Slf4j(topic = "TwoPhaseTermination")
class TwoPhaseTermination{
    private Thread monitor;

    public void start(){
        monitor = new Thread(()->{
            while (true){
                Thread current = Thread.currentThread();
                if (current.isInterrupted()){
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    current.interrupt();
                }
            }

        });
        monitor.start();

    }
    public void stop(){
        monitor.interrupt();
    }
}
