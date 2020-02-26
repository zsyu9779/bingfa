package designpattern;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Zsyu
 * @Date: 20-2-25 下午11:57
 */

//犹豫模式
@Slf4j(topic = "main")
public class BalkingTest {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination2 twoPhaseTermination = new TwoPhaseTermination2();
        TwoPhaseTermination2 twoPhaseTermination2 = new TwoPhaseTermination2();

        twoPhaseTermination.start();
        twoPhaseTermination2.start();

        Thread.sleep(5500);
        log.debug("停止线程");
        twoPhaseTermination.stop();
        Thread.sleep(1000);
        log.debug("停止线程2");
        twoPhaseTermination2.stop();

    }
}
@Slf4j(topic = "TwoPhaseTermination")
class TwoPhaseTermination2{
    private Thread monitor;
    private volatile boolean stop = false;

    private boolean starting = false;
    public void start(){
        synchronized (this){
            if (starting){
                return;
            }

            starting = true;
        }
        monitor = new Thread(()->{
            while (true){
                Thread current = Thread.currentThread();
                if (stop){
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {

                }
            }

        });
        monitor.start();

    }
    public void stop(){
        stop = true;

        monitor.interrupt();
    }
}