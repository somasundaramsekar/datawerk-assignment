package me.soma.datawerks.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import me.soma.datawerks.service.FileUploadService;

@Controller
public class FileUploadController {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);


	@Autowired
	FileUploadService fileUploadService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "upload";
	}

	@PostMapping("/upload") 
	public String singleFileUpload(HttpServletRequest file,
			RedirectAttributes redirectAttributes) throws FileUploadException {
		logger.info("Hit /upload");
		try{
			if (! fileUploadService.isMultiPart(file)) {
				redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
				return "redirect:uploadStatus";
			}
			String fileName = fileUploadService.fileUpload(file);
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + fileName + "'");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/uploadStatus";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

}
