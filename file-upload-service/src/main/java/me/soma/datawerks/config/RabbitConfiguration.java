package me.soma.datawerks.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by somasundar.sekar on 3/12/2017.
 */
@Configuration
public class RabbitConfiguration {

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;
    @Value("${spring.rabbitmq.username}")
    private String rabbitUserName;
    @Value("${spring.rabbitmq.password}")
    private String rabbitPassword;
    @Value("${spring.rabbitmq.port}")
    private int rabbitPort;
    @Value("${upload.complete.exchange}")
    public String uploadCompleteExchangeName;
    @Value("${upload.complete.queue}")
    private String uploadCompleteQueue;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitHost);
        connectionFactory.setUsername(rabbitUserName);
        connectionFactory.setPassword(rabbitPassword);
        connectionFactory.setVirtualHost(rabbitUserName);
        connectionFactory.setPort(rabbitPort);
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public Queue uploadNotificationQueue() {
        return new Queue(uploadCompleteQueue, true);
    }

    @Bean
    public DirectExchange exchange() {
        DirectExchange exchange = new DirectExchange(uploadCompleteExchangeName);
        return exchange;
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(uploadNotificationQueue()).to(exchange()).with("");
    }

    @Bean(name="rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory listenerFactory(){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        return factory;
    }
}
