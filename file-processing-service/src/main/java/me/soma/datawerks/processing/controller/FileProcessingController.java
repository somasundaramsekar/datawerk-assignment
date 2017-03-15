package me.soma.datawerks.processing.controller;

import me.soma.datawerks.processing.domain.services.FileProcessingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api")
public class FileProcessingController {

	@Autowired
	FileProcessingService fileProcessingService;

	@GetMapping(value="/notify")
	@ResponseBody
	public String notifyUploadComplete(@RequestParam(value="fileName") String fileName){
		fileProcessingService.processFile(fileName);
		return fileName+" processing initiated";
	}


}
