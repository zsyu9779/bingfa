package testbiased;


import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;

/**
 *批量重偏向
 * 如果对象虽然被多个线程访问,但没有竞争,这时偏向了线程 T1 的对象仍有机会重新偏向 T2,重偏向会重置对象
 * 的 Thread ID
 * 当撤销偏向锁阈值超过 20 次后,jvm 会这样觉得,我是不是偏向错了呢,于是会在给这些对象加锁时重新偏向至
 * 加锁线程
 *
 * 批量撤销
 * 当撤销偏向锁阈值超过 40 次后,jvm 会这样觉得,自己确实偏向错了,根本就不该偏向。于是整个类的所有对象
 * 都会变为不可偏向的,新建的对象也是不可偏向的
 *
 * */

public class TestBiased2 {
    public static void main(String[] args) {
        Vector<Cat> cats = new Vector<>();
        Thread t1 = new Thread(()->{
            for (int i = 0;i<30;i++){
                Cat cat = new Cat();
                cats.add(cat);
                synchronized (cat){
                    System.out.println(ClassLayout.parseInstance(cat).toPrintable());
                }
            }
            synchronized (cats){
                cats.notify();
            }
        });
        t1.start();

        Thread t2 = new Thread(()->{
            synchronized (cats){
                try {
                    cats.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("=================================================================================================================================================>");
            System.out.println("=================================================================================================================================================>");
            System.out.println("=================================================================================================================================================>");
            for (int i = 0;i<30;i++){
                Cat cat = cats.get(i);
                if (i==19){
                    System.out.println("=====================================开始重偏向==========================================");
                }
                System.out.println(i+"\t"+ClassLayout.parseInstance(cat).toPrintable());

                synchronized (cat){
                    System.out.println(i+"\t"+"synchronized"+"\t"+ClassLayout.parseInstance(cat).toPrintable());
                }
                System.out.println(i+"\t"+"finished"+"\t"+ClassLayout.parseInstance(cat).toPrintable());

            }
            synchronized (cats){
                cats.notify();
            }
        });
        t2.start();

    }
}
class Cat{

}