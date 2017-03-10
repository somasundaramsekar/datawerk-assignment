package me.soma.datawerks.serviceImpl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import me.soma.datawerks.service.FileUploadService;

@Component
public class FileUploadServiceImpl implements FileUploadService {
	
	@Autowired
	Environment env;

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
				File folderName = new File(env.getProperty("upload.folder")+fileName);
				OutputStream out = new FileOutputStream(folderName);
				IOUtils.copy(stream, out);
				stream.close();
				out.close();
			}
		}
		return fileName;
	}

	@Override
	public boolean isMultiPart(HttpServletRequest file) {
		return ServletFileUpload.isMultipartContent(file);
	}
	
	void checkValidExtension(String fileName){
		String fileExtentions = ".mkv";
		String substring = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
		if(! fileExtentions.contains(substring)){
			throw new IllegalArgumentException("File format is not correct");
		}
	}

}
