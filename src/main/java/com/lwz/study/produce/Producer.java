package com.lwz.study.produce;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息队列生产者
 * @author lwz
 */
@Slf4j
public class Producer {

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
     * 交换机名称
     */
    public static final String EXCHANGE_NAME = "exchangeDemo";

    /**
     * 队列名称
     */
    public static final String QUEUE_NAME = "queueDemo";

    /**
     * 路由键
     */
    public static final String ROUTING_KEY = "routingKeyDemo";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 设置ip地址
        factory.setHost(IP_ADDRESS);
        // 设置端口后
        factory.setPort(PORT);
        // 设置用户名
        factory.setUsername(USER_NAME);
        // 设置密码
        factory.setPassword(PASSWORD);
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 创建一个 type = "direct" 持久化的、非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);
        // 创建一个持久化、非排他的、非自动删除的队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY) ;
        // 发送一条持久化的消息
        String message = "Hello World !";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes( )) ;
        // 关闭资源
        channel.close();
        connection.close();
        log.info(">>>>>>>>>>>>>>消息发送成功："+ message);
    }
}
