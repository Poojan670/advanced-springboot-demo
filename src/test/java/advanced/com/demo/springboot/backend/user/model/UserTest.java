package advanced.com.demo.springboot.backend.user.model;

import advanced.com.demo.springboot.backend.core.model.CreateInfo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

class UserTest {

    /**
     * Method under test: {@link User#onCreate()}
     */
    @Test
    void testOnCreate2() {
        CreateInfo createInfo = mock(CreateInfo.class);
        doNothing().when(createInfo).setCreatedAtAd((LocalDateTime) any());
        doNothing().when(createInfo).setUpdatedAtAd((LocalDateTime) any());
        createInfo.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        User user = new User();
        user.setCreateInfo(createInfo);
        user.onCreate();
        verify(createInfo).setCreatedAtAd((LocalDateTime) any());
        verify(createInfo).setUpdatedAtAd((LocalDateTime) any());
    }

    /**
     * Method under test: {@link User#onUpdate()}
     */
    @Test
    void testOnUpdate3() {
        CreateInfo createInfo = mock(CreateInfo.class);
        doNothing().when(createInfo).setUpdatedAtAd((LocalDateTime) any());
        (new User(123L, "janedoe", "jane.doe@example.org", "iloveyou", new ArrayList<>(), true, createInfo)).onUpdate();
        verify(createInfo).setUpdatedAtAd((LocalDateTime) any());
    }
}

