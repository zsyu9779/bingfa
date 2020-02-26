package designpattern;

/**
 * @Author: Zsyu
 * @Date: 20-2-26 下午3:13
 */
/**
 * Double check locking
 * */

    public final class SingletonTest2 {
        private SingletonTest2() { }
        // 问题1:解释为什么要加 volatile ?  加一层写屏障 防止指令重排
        private static volatile SingletonTest2 INSTANCE = null;
        // 问题2:对比实现3, 说出这样做的意义  性能提高
        public static SingletonTest2 getInstance() {
            if (INSTANCE != null) {

                return INSTANCE;
            }
            synchronized (SingletonTest2.class) {
                // 问题3:为什么还要在这里加为空判断, 之前不是判断过了吗  防止首次创建实例时的并发问题
                if (INSTANCE != null) {
// t2
                    return INSTANCE;
                }
                INSTANCE = new SingletonTest2();
                return INSTANCE;
            }
        }
    }

