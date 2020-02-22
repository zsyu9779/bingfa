package waitnotify;

/**
 sleep(long n) 和 wait(long n) 的区别
 1) sleep 是 Thread 方法,而 wait 是 Object 的方法
 2) sleep 不需要强制和 synchronized 配合使用,但 wait 需要和 synchronized 一起用
 3) sleep 在睡眠的同时,不会释放对象锁的,但 wait 在等待的时候会释放对象锁
 4) 它们状态 TIMED_WAITING
 */
public class Test1 {
    final static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (obj) {
                System.out.println("1111执行....");
                try {
                    obj.wait(); // 让线程在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("111111其它代码....");
            }
        },"t1").start();
        new Thread(() -> {
            synchronized (obj) {
                System.out.println("222222执行....");
                try {
                    obj.wait(); // 让线程在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("2222其它代码....");
            }
        },"t2").start();

        Thread.sleep(500);
        System.out.println("唤醒 obj 上其它线程");
        synchronized (obj) {
            obj.notify(); // 唤醒obj上一个线程
            //obj.notifyAll(); // 唤醒obj上所有等待线程
        }
    }
}
