package designpattern;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * @Author: Zsyu
 * @Date: 20-2-22 下午1:10
 */
@Slf4j(topic = "GuardedSuspension")
public class GuardedSuspension {
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(()->{

            log.debug("等待结果");
            List<String> list = (List<String>) guardedObject.get(1000);
            if (list==null){
                log.debug("等待超时");
            }
            else{
                log.debug("结果{}",list);

            }

        },"t1").start();


        new Thread(()->{

            log.debug("执行下载");
            List<String> list = null;
            try {
                Thread.sleep(1000);
                list = Downloader.download();
                guardedObject.complete(list);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"t2").start();

    }
}

class GuardedObject{

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