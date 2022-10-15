package advanced.com.demo.springboot.backend.core.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import advanced.com.demo.springboot.backend.helper.MediaHelper.FileServiceHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {MediaController.class})
@ExtendWith(SpringExtension.class)
class MediaControllerTest {
    @MockBean
    private FileServiceHelper fileServiceHelper;

    @Autowired
    private MediaController mediaController;

    /**
     * Method under test: {@link MediaController#getImageWithMediaType(String)}
     */
    @Test
    void testGetImageWithMediaType() throws Exception {
        when(fileServiceHelper.getImageWithMediaType((String) any())).thenReturn("AAAAAAAA".getBytes("UTF-8"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/core-app/getImage/{imageName:.+}",
                "U");
        MockMvcBuilders.standaloneSetup(mediaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("image/jpeg"))
                .andExpect(MockMvcResultMatchers.content().string("AAAAAAAA"));
    }

    /**
     * Method under test: {@link MediaController#getFileWithMediaType(String)}
     */
    @Test
    void testGetFileWithMediaType() throws Exception {
        when(fileServiceHelper.getFileWithMediaType((String) any())).thenReturn("AAAAAAAA".getBytes("UTF-8"));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/core-app/getFile/{fileName:.+}",
                "U");
        MockMvcBuilders.standaloneSetup(mediaController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("multipart/form-data"))
                .andExpect(MockMvcResultMatchers.content().string("AAAAAAAA"));
    }
}

