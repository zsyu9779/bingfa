package testbiased;

import org.openjdk.jol.info.ClassLayout;

/**
 * @Author: Zsyu
 * @Date: 20-2-21 下午10:44
 */
public class TestBiased {
    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
        new Thread(()->{

            System.out.println("a:"+ClassLayout.parseInstance(dog).toPrintable());

            synchronized (dog){
                System.out.println("b:"+ClassLayout.parseInstance(dog).toPrintable());
            }

            System.out.println("c:"+ClassLayout.parseInstance(dog).toPrintable());
            synchronized (TestBiased.class){
                TestBiased.class.notify();
            }

        }).start();
        new Thread(()->{
            synchronized (TestBiased.class){
                try {
                    TestBiased.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("a:"+ClassLayout.parseInstance(dog).toPrintable());
            System.out.println("目前还属于偏向锁+++++++++++++++++++++++++++++++++++++++++++++");

            synchronized (dog){
                System.out.println("b:"+ClassLayout.parseInstance(dog).toPrintable());
                System.out.println("变成了轻量级锁");
            }

            System.out.println("c:"+ClassLayout.parseInstance(dog).toPrintable());
            System.out.println("变成了Normal状态");


        }).start();




    }

}
class Dog{

}
