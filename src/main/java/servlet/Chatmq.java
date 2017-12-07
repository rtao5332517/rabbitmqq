package servlet;

import com.rabbitmq.client.*;


import java.io.IOException;

import java.util.Scanner;

public class Chatmq {
    //队列名称

    public void Send() throws Exception {
        String EXCHANGE_NAME = "xxx";
        //创建链接
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("119.23.243.0");// MQ的IP
        factory.setPort(5672);// MQ端口
        factory.setUsername("admin");// MQ用户名
        factory.setPassword("admin");// MQ密码
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明转发器和类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        System.out.println("欢迎加入群聊，请输入您的昵称：");
        String username = new Scanner(System.in

        ).nextLine();
                while (true) {
                    String message = new Scanner(System.in

                    ).nextLine();
                    if ("exit".equals(message)) {
                        channel.close();
                        connection.close();
                        return;
                    }
                    message = username + ":" + message;


                    channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
                }

    }

//消费者
    public void Recv() throws Exception {
        // 队列名称 创建连接和 频道
        String EXCHANGE_NAME = "xxx";
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("119.23.243.0");// MQ的IP
        factory.setPort(5672);// MQ端口
        factory.setUsername("admin");// MQ用户名
        factory.setPassword("admin");// MQ密码

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明转发器和类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 创建一个非持久的、唯一的且自动删除的队列
        String queueName = channel.queueDeclare().getQueue();
        // 为转发器指定队列，设置binding
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(message);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }


}
