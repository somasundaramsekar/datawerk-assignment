package me.soma.datawerks.processing.listener;

import me.soma.datawerks.processing.domain.services.FileProcessingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class UploadNotificationListener {

	@Value("${upload.complete.queue}")
	private String uploadNotificationQueueName;

	@Autowired
	FileProcessingService fileProcessingService;

	//could it be? @RabbitListener(queues = "#{upload.complete.queue}")
	@RabbitListener(queues = "uploadNotificationQueue")
    public void process(byte[] msg) {
		fileProcessingService.processFile(new String(msg));
	}
}