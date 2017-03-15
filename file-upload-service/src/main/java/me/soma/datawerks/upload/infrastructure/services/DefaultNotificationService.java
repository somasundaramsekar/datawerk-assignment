package me.soma.datawerks.upload.infrastructure.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import me.soma.datawerks.upload.domain.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by somasundar.sekar on 3/13/2017.
 */
@Service
@RefreshScope
public class DefaultNotificationService implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultNotificationService.class);


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${upload.complete.exchange}")
    private String uploadCompleteExchangeName;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @HystrixCommand(fallbackMethod = "fallbackToMessageQ")
    public void notifyComplete(String fileName) {


        ResponseEntity<String> exchange =
                this.restTemplate.exchange(
                        "http://FILE-PROCESSING-SERVICE/api/notify/"+fileName,
                        HttpMethod.GET,
                        null,
                        String.class ,
                        (Object) "mstine");

        logger.info("Processing Initaion result >> "+exchange.getBody());
    }

    @Override
    public void fallbackToMessageQ(String fileName) {
        rabbitTemplate.convertAndSend(uploadCompleteExchangeName, "", fileName.getBytes());
    }

}
