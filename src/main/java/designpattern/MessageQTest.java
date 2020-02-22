package designpattern;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * @Author: Zsyu
 * @Date: 20-2-23 上午1:41
 */
@Slf4j(topic = "MessageQTest")

public class MessageQTest {

    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(5);

        for (int i = 0;i<10;i++){
            int id = i;

            new Thread(()->{
                messageQueue.put(new Message(id,"消息"+id));
            },"生产者"+i).start();
        }
        for (int i = 0;i<2;i++){
            new Thread(()->{
                while (true){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Message message = messageQueue.take();
                }
            },"消费者"+i).start();
        }
    }
}
@Slf4j(topic = "MessageQueue")

class MessageQueue{
    //消息队列集合
    private LinkedList<Message> linkedList= new LinkedList<>();
    //队列容量
    private int capcity;

    public MessageQueue(int capcity) {
        this.capcity = capcity;
    }

    public Message take(){

        synchronized (linkedList){
            while (linkedList.isEmpty()){
                try {
                    log.debug("队列为空，消费者等待");

                    linkedList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = linkedList.removeFirst();
            log.debug("已消费消息{}",message);
            linkedList.notifyAll();

            return message;
        }

    }
    public void put(Message message){

        synchronized (linkedList){
            while (linkedList.size()==capcity){
                try {
                    linkedList.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            linkedList.addLast(message);
            log.debug("生产消息{}",message);
            linkedList.notifyAll();

        }

    }
}

class Message{
    private int id;
    private Object message;

    public Message(int id, Object message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public Object getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", message=" + message +
                '}';
    }
}