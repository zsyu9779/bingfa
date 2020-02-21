package reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Zsyu
 * @Date: 20-2-20 下午9:58
 */

//可重入
public class Test1 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {

        lock.lock();
        try {
            System.out.println("main");
            m1();
        }finally {
            lock.unlock();
        }
    }

    public static void m1(){
        lock.lock();
        try {
            System.out.println("m1");
            m2();
        }finally {
            lock.unlock();
        }
    }


    public static void m2(){
        lock.lock();
        try {
            System.out.println("m2");
        }finally {
            lock.unlock();
        }
    }
}
