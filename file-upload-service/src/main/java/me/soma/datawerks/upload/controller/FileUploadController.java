package me.soma.datawerks.upload.controller;

import me.soma.datawerks.upload.domain.services.FileUploadService;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileUploadController {

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);


	private final RestTemplate restTemplate;
	private final FileUploadService fileUploadService;

	@Autowired
	public FileUploadController(RestTemplate restTemplate, FileUploadService fileUploadService) {
		this.restTemplate = restTemplate;
		this.fileUploadService = fileUploadService;
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "upload";
	}

	@PostMapping("/upload")
	public String singleFileUpload(HttpServletRequest file,
								   RedirectAttributes redirectAttributes) throws FileUploadException {
		logger.info("Hit /upload");
		String fileName = null;
		try{
			if (! fileUploadService.isMultiPart(file)) {
				redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
				return "redirect:uploadStatus";
			}
			fileName = fileUploadService.fileUpload(file);
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded file in  '" + fileName + "'");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/status";
	}

	@GetMapping("/status")
	public String uploadStatus() {
		return "uploadStatus";
	}

}
