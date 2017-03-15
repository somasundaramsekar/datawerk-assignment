package me.soma.datawerks.processing.domain.services;

import org.springframework.stereotype.Service;


@Service
public interface FileProcessingService {
	void processFile(String inputFile);
}
