package volatiletest;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Zsyu
 * @Date: 20-2-24 下午3:20
 */
@Slf4j(topic = "volatile")
public class Test {
     static boolean run = true;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(()->{
            log.debug("start");
            while (run){
                System.out.println(run);


            }

        });
        thread.start();
        Thread.sleep(1000);
        log.debug("stop");
        run = false;
    }
}
