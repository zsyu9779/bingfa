package reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * @Author: Zsyu
 * @Date: 20-2-20 下午10:08
 */

//可打断性
public class Test2 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

       Thread t1 = new Thread(()->{
            try {
                System.out.println("尝试获得锁");
                lock.lockInterruptibly();

            }catch (Exception e){
                e.printStackTrace();
                System.out.println("没有获得锁");
                return;
            }

        },"t1");

        lock.lock();
        t1.start();

        sleep(1000);
        System.out.println("打断t1");
        t1.interrupt();

    }
}
