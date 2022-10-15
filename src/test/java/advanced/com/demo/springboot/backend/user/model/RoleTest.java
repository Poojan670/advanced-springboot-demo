package advanced.com.demo.springboot.backend.user.model;

import advanced.com.demo.springboot.backend.core.model.CreateInfo;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

class RoleTest {
    /**
     * Method under test: {@link Role#onCreate()}
     */
    @Test
    void testOnCreate() throws Exception {
        CreateInfo createInfo = mock(CreateInfo.class);
        doNothing().when(createInfo).setCreatedAtAd((LocalDateTime) any());
        doNothing().when(createInfo).setUpdatedAtAd((LocalDateTime) any());
        createInfo.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        Role role = new Role();
        role.setCreateInfo(createInfo);
        role.onCreate();
        verify(createInfo).setCreatedAtAd((LocalDateTime) any());
        verify(createInfo).setUpdatedAtAd((LocalDateTime) any());
    }

    /**
     * Method under test: {@link Role#onUpdate()}
     */
    @Test
    void testOnUpdate3() {
        CreateInfo createInfo = mock(CreateInfo.class);
        doNothing().when(createInfo).setUpdatedAtAd((LocalDateTime) any());
        (new Role(123L, "Name", createInfo)).onUpdate();
        verify(createInfo).setUpdatedAtAd((LocalDateTime) any());
    }
}

