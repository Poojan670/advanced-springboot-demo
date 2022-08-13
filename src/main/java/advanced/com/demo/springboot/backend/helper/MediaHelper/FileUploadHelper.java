package advanced.com.demo.springboot.backend.helper.MediaHelper;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileUploadHelper {
    private final String documentUploadPath = "static/file/";
    private final String imageUploadPath = "static/image/";

    private final Path imageStorageLocation;
    private final Path documentStorageLocation;


    public FileUploadHelper() throws IOException {
        this.documentStorageLocation = Paths.get(this.documentUploadPath).toAbsolutePath().normalize();
        Files.createDirectories(documentStorageLocation);
        this.imageStorageLocation = Paths.get(this.imageUploadPath).toAbsolutePath().normalize();
        Files.createDirectories(this.imageStorageLocation);

    }

    public String uploadFile(String directoryName, String fileName, MultipartFile file){
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        String name = UUID.randomUUID() +"."+fileExtension;

        try {
            if (directoryName.equals("file")){
                Files.copy(file.getInputStream(), Paths.get(documentStorageLocation+File.separator+name), StandardCopyOption.REPLACE_EXISTING);
            } else if (directoryName.equals("image")) {
                Files.copy(file.getInputStream(), Paths.get(imageStorageLocation+File.separator+name), StandardCopyOption.REPLACE_EXISTING);
            }else{
                throw new RuntimeException("Please provide correct directory name, can be either 'file' or 'image' ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/core-app/getImage/")
                .path(name)
                .toUriString();
    }
}
