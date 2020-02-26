package designpattern;

/**
 * @Author: Zsyu
 * @Date: 20-2-26 下午3:24
 */

//静态内部类式单例
public final class SingletonTest3 {
    private SingletonTest3() { }
    // 问题1:属于懒汉式还是饿汉式 懒汉
    private static class LazyHolder {
        static final SingletonTest3 INSTANCE = new SingletonTest3();
    }
    // 问题2:在创建时是否有并发问题 无 调用方法才进行类加载
    public static SingletonTest3 getInstance() {
        return LazyHolder.INSTANCE;
    }
}