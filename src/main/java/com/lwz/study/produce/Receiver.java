package com.lwz.study.produce;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 消费者
 * @author lwz
 */
@Slf4j
public class Receiver {

    /**
     * ip地址
     */
    public static final String IP_ADDRESS = "116.62.179.12";
    /**
     * rabbitMQ 默认端口号
     */
    public static final int PORT = 5672;

    /**
     * 用户名
     */
    public static final String USER_NAME = "root";

    /**
     * 密码
     */
    public static final String PASSWORD = "rabbitmq123456";

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "queueDemo";

    public static void main(String[] args) throws Exception {

        Address[] addresses = new Address[]{
                new Address(IP_ADDRESS, PORT)
        };
        ConnectionFactory factory = new ConnectionFactory();
        // 设置用户名
        factory.setUsername(USER_NAME);
        // 设置密码
        factory.setPassword(PASSWORD);
        Connection connection = factory.newConnection(addresses);
        Channel channel = connection.createChannel();
        channel.basicQos(64);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                log.info("receive message:" + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, consumer);
        //等待回调函数执行完毕之后 关闭资源
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }
}
