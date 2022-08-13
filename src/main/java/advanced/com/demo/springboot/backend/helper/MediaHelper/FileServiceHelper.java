package advanced.com.demo.springboot.backend.helper.MediaHelper;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class FileServiceHelper {

    public final String fileDirectoryPath = "static/file/";
    public final String imageDirectoryPath = "static/image/";

    public  byte[] getImageWithMediaType(String imageName) throws IOException {
        Path destination =   Paths.get(imageDirectoryPath+"\\"+imageName);// retrieve the image by its name

        return IOUtils.toByteArray(destination.toUri());
    }

    public  byte[] getFileWithMediaType(String fileName) throws IOException {
        Path destination =   Paths.get(fileDirectoryPath+"\\"+fileName);// retrieve the file by its name

        return IOUtils.toByteArray(destination.toUri());
    }

}
