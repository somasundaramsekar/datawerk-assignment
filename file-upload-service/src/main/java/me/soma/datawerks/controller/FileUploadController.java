package me.soma.datawerks.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileUploadController {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    //Save the uploaded file to this folder
    private static String UPLOADED_FOLDER = "N://";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "upload";
    }
  
    

    @PostMapping("/upload") // //new annotation since 4.3
    public String singleFileUpload(HttpServletRequest file,
                                   RedirectAttributes redirectAttributes) throws FileUploadException {
    	logger.info("Hit /upload");
    	
    	try{
    	boolean isMultipart = ServletFileUpload.isMultipartContent(file);
    	
    	logger.info("is multipart file >> "+isMultipart);

        if (! isMultipart) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
    	ServletFileUpload upload = new ServletFileUpload();
        // Parse the request
        FileItemIterator iter = upload.getItemIterator(file);
        while (iter.hasNext()) {
            FileItemStream item = iter.next();
            String fileExtentions = ".csv";
            String fileName =   item.getName();
            
            int lastIndex = fileName.lastIndexOf('.');
        	String substring = fileName.substring(lastIndex, fileName.length());
        	/*if(! fileExtentions.contains(substring)){
        		throw new IllegalArgumentException("File format is not correct");
        	}*/
        			/*byte[] bytes = item.getName().getBytes();
                    Path path = Paths.get(UPLOADED_FOLDER + fileName);
                    Files.write(path, bytes);*/
                    InputStream stream = item.openStream();
            if (!item.isFormField()) {
                String filename = item.getName();
                // Process the input stream
                OutputStream out = new FileOutputStream(filename);
                IOUtils.copy(stream, out);
                stream.close();
                out.close();
            }
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded '" + fileName + "'");
        }
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
