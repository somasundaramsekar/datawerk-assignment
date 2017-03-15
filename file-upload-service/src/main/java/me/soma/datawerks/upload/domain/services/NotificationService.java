package me.soma.datawerks.upload.domain.services;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

/**
 * Created by somasundar.sekar on 3/14/2017.
 */
@Service
public interface NotificationService {
    @HystrixCommand(fallbackMethod = "fallbackToMessageQ")
    void notifyComplete(String fileName);

    void fallbackToMessageQ(String fileName);
}
