package me.soma.datawerks.upload.infrastructure.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import me.soma.datawerks.upload.domain.services.FileUploadService;
import me.soma.datawerks.upload.domain.services.NotificationService;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@RefreshScope
public class StreamingFileUploadService implements FileUploadService {

	@Autowired
	private NotificationService notificationService;

	@Value("${upload.folder}")
	private String path;


	@Override
	public String fileUpload(HttpServletRequest file) throws FileUploadException, IOException {
		
		String fileName = null;
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(file);
		while (iter.hasNext()) {
			FileItemStream item = iter.next();
			fileName =   item.getName();
			checkValidExtension(fileName);
			InputStream stream = item.openStream();
			if (!item.isFormField()) {
				File folderName = new File(path+fileName);
				OutputStream out = new FileOutputStream(folderName);
				IOUtils.copy(stream, out);
				stream.close();
				out.close();
			}
		}
		notificationService.notifyComplete(fileName);
		return fileName;
	}


	@Override
	public boolean isMultiPart(HttpServletRequest file) {
		return ServletFileUpload.isMultipartContent(file);
	}
	
	void checkValidExtension(String fileName){
		String fileExtentions = ".csv";
		String substring = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
		if(! fileExtentions.contains(substring)){
			throw new IllegalArgumentException("File format is not correct");
		}
	}

}
