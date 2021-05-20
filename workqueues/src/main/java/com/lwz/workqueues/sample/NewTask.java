package com.lwz.workqueues.sample;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

/**
 * 基于工作队列
 * @author lwz
 */
public class NewTask {

    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("116.62.179.12");
        factory.setUsername("root");
        factory.setPassword("rabbitmq123456");
        try(Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()
        ) {
            // 创建一个队列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            // 发送消息
            String message = String.join(" ", args);
            channel.basicPublish("", QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes("utf-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }

    }

}
