package com.lwz.publishsubscribe.sample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * 基于发布订阅模式实现日志消费者
 * @author lwz
 */
public class ReceiveLogs {

    private final static String EXCHANGE_NAME = "logs";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("116.62.179.12");
        factory.setUsername("root");
        factory.setPassword("rabbitmq123456");
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建channel
        Channel channel = connection.createChannel();
        // 声明一个fanout类型的交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 声明一个随机队列
        String queue = channel.queueDeclare().getQueue();
        // 绑定
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> { });
    }
}
