package designpattern;

/**
 * @Author: Zsyu
 * @Date: 20-2-26 下午2:52
 */
public class SingletonTest1 {
    private SingletonTest1() { }
    private static SingletonTest1 INSTANCE = null;
    // 分析这里的线程安全, 并说明有什么缺点
    public static synchronized SingletonTest1 getInstance() {
        if( INSTANCE != null ){
            return INSTANCE;
        }
        INSTANCE = new SingletonTest1();
        return INSTANCE;
    }
}
