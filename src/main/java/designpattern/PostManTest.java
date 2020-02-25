package designpattern;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 而HashTable虽然是线程安全的，但是是通过整个来加锁的方式，当一个线程在写操作的时候，另外的线程则不能进行读写。
 *
 * 而ConcurrentHashMap则可以支持并发的读写。跟1.7版本相比，1.8版本又有了很大的变化，已经抛弃了Segment的概念，
 * 虽然源码里面还保留了，也只是为了兼容性的考虑。
 */

/**
 * @Author: Zsyu
 * @Date: 20-2-22 下午10:15
 */
@Slf4j(topic = "c.main")
public class PostManTest {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0;i<3;i++){
            new People().start();
        }
        Thread.sleep(1000);

        for (Integer id : Mailboxes.getIds()) {
            new Postman(id,"内容"+id).start();
        }

    }
}

@Slf4j(topic = "c.people")
class People extends Thread{
    @Override
    public void run() {

        GuardedObject1 guardedObject1 = Mailboxes.createGuardObject();
        log.debug("开始收信 id：{}",guardedObject1.getId());
        Object mail = guardedObject1.get(5000);
        log.debug("收到信 id：{}，内容 {}",guardedObject1.getId(),mail);

    }
}

@Slf4j(topic = "c.postman")
class Postman extends Thread{
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject1 guardedObject1 = Mailboxes.getguardedObject1(id);
        log.debug("开始送信 id：{}",guardedObject1.getId());
        guardedObject1.complete(mail);
        log.debug("送达信 id：{}，内容 {}",guardedObject1.getId(),mail);

    }
}

class Mailboxes {
    private static Map<Integer,GuardedObject1> boxs = new ConcurrentHashMap<>();

    private static Map<Integer,GuardedObject1> boxss = new Hashtable<>();
    private static Map<Integer,GuardedObject1> boxsss = new HashMap<>();


    private static int id = 1;

    private static synchronized int generateId(){

        return id++;
    }

    public static GuardedObject1 getguardedObject1(int id){
        return boxs.remove(id);
    }

    public static  GuardedObject1 createGuardObject(){
        synchronized (boxs){
            GuardedObject1 go = new GuardedObject1(generateId());
            boxs.put(go.getId(),go);
            return go;
        }
    }

    public static  Set<Integer> getIds(){
        return boxs.keySet();
    }

}


class GuardedObject1{

    private int id;


    public int getId() {
        return id;
    }

    public GuardedObject1(int id) {
        this.id = id;
    }

    //结果
    private Object response;

    //获取结果
    //timeout表示等待时间
    public Object get(long timeout){
        synchronized (this){
            //记录开始时间
            long begin = System.currentTimeMillis();
            long pasttime = 0;
            while (response==null){
                long waittime = timeout-pasttime;
                //经历的时间超出了最大等待时间，退出循环
                if (waittime<=0){
                    break;
                }
                try {
                    this.wait(waittime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pasttime = System.currentTimeMillis()-begin;
            }
            return response;
        }
    }

    //产生结果
    public void complete(Object response){
        synchronized (this){
            //给结果成员变量赋值
            this.response = response;
            this.notifyAll();

        }
    }
}