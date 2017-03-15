package me.soma.datawerks.processing.infrastructure.services;

import me.soma.datawerks.processing.domain.models.Observation;
import me.soma.datawerks.processing.domain.services.FileProcessingService;
import me.soma.datawerks.processing.infrastructure.persistance.ObservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Component
@RefreshScope
public class StreamingFileProcessingService implements FileProcessingService {

	private static final Logger logger = LoggerFactory.getLogger(StreamingFileProcessingService.class);

	private ExecutorService executorService = Executors.newSingleThreadExecutor();

	@Autowired
	ObservationRepository observationRepo;

	@Value("${upload.folder}")
	private String fileLookupPath;

	@Override
	public void processFile(String fileName) {

		executorService.execute(new Runnable() {
			public void run() {
				processInputFile(fileName);
			}
		});
		executorService.shutdown();
	}

	private void processInputFile(String fileName ) {
		String inputFilePath= fileLookupPath + fileName;
		try{
			File inputF = new File(inputFilePath);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			br.lines().skip(1)
					.map(line -> line.split(","))
					.filter(record -> record != null && record.length == 4)
					.forEach(record -> mapToObservation.apply(record));
			br.close();
			logger.info("Size of the File-->"+observationRepo.count());
		} catch (IOException e) {
			logger.error("Error occured while processing: ",e);
		}
	}

	private Function<String[], Observation> mapToObservation = (String[] record) -> {
		try {
			Observation observation = new Observation(
					Integer.valueOf(record[0]),
					record[1].toLowerCase(),
					record[2]
			);
			Observation obs = observationRepo.save(observation);

		}catch (IllegalArgumentException e) {
			logger.debug("Ignoring record: Reason: ",e);
		}
		return null;
	};
}
