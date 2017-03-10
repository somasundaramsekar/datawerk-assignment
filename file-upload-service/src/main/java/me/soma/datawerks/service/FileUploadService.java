package me.soma.datawerks.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;

@Service
public interface FileUploadService {

	public String fileUpload(HttpServletRequest file)throws FileUploadException, IOException ;

	public boolean isMultiPart(HttpServletRequest file);
	

}
