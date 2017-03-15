package me.soma.datawerks.upload.domain.services;

import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by somasundar.sekar on 3/14/2017.
 */
@Service
public interface FileUploadService {
    String fileUpload(HttpServletRequest file) throws FileUploadException, IOException;

    boolean isMultiPart(HttpServletRequest file);
}
