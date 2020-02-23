package parkunpark;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;


/**
 * 与 Object 的 wait & notify 相比:
 * wait,notify 和 notifyAll 必须配合 Object Monitor 一起使用,而 park,unpark 不必
 * park & unpark 是以线程为单位来【阻塞】和【唤醒】线程,而 notify 只能随机唤醒一个等待线程,notifyAll
 * 是唤醒所有等待线程,就不那么【精确】
 * park & unpark 可以先 unpark,而 wait & notify 不能先 notify
 * */
@Slf4j(topic = "c.TestParkUnpark")
public class TestParkUnpark {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("start...");
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("park...");
            LockSupport.park();
            log.debug("resume...");
        }, "t1");
        t1.start();

        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("unpark...");
        LockSupport.unpark(t1);
    }
}
