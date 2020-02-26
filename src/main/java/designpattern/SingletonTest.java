package designpattern;

import java.io.Serializable;


//饿汉单例
/**
 * @Author: Zsyu
 * @Date: 20-2-26 下午2:14
 */
// 问题1:为什么加 final  A：防止子类覆盖方法破坏单例
// 问题2:如果实现了序列化接口, 还要做什么来防止反序列化破坏单例 A:加一个方法readResolve(26行)

public final class SingletonTest implements Serializable {
        // 问题3:为什么设置为私有? 是否能防止反射创建新的实例? A：防止别的类调用，破坏单例。 不能

        private SingletonTest() {}
        // 问题4:这样初始化是否能保证单例对象创建时的线程安全?  能，在类加载阶段就完成了初始化

        private static final SingletonTest INSTANCE = new SingletonTest();
        // 问题5:为什么提供静态方法而不是直接将 INSTANCE 设置为 public, 说出你知道的理由  提供更好的封装性，实现对泛型的支持

        public static SingletonTest getInstance() {
            return INSTANCE;
        }
        public Object readResolve() {
            return INSTANCE;
        }

   /**
    * 枚举单例
    * */

    // 问题1:枚举单例是如何限制实例个数的  实例是枚举类里的一个静态成员变量
    // 问题2:枚举单例在创建时是否有并发问题  无
    // 问题3:枚举单例能否被反射破坏单例  不能
    // 问题4:枚举单例能否被反序列化破坏单例  能
    // 问题5:枚举单例属于懒汉式还是饿汉式  饿汉
    // 问题6:枚举单例如果希望加入一些单例创建时的初始化逻辑该如何做  加一个构造方法
    enum Singleton {
        INSTANCE;
    }



    }

