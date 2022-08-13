package advanced.com.demo.springboot.backend.core.controller;

import advanced.com.demo.springboot.backend.helper.MediaHelper.FileServiceHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/core-app")
@Slf4j
public class MediaController {
    private final FileServiceHelper fileServiceHelper;


    @GetMapping(
            value = "getImage/{imageName:.+}",
            produces = {MediaType.IMAGE_JPEG_VALUE,MediaType.IMAGE_GIF_VALUE,MediaType.IMAGE_PNG_VALUE}
    )
    public @ResponseBody byte[] getImageWithMediaType(@PathVariable(name = "imageName") String fileName) throws IOException {
        return this.fileServiceHelper.getImageWithMediaType(fileName);
    }

    @GetMapping(
            value = "getFile/{fileName:.+}",
            produces = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public @ResponseBody byte[] getFileWithMediaType(@PathVariable(name = "fileName") String fileName) throws IOException {
        return this.fileServiceHelper.getFileWithMediaType(fileName);
    }
}