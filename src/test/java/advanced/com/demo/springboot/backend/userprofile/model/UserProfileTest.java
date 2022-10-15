package advanced.com.demo.springboot.backend.userprofile.model;

import advanced.com.demo.springboot.backend.core.model.CreateInfo;
import advanced.com.demo.springboot.backend.user.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UserProfileTest {
    /**
     * Method under test: {@link UserProfile#onCreate()}
     */
    @Test
    void testOnCreate() {
        UserProfile userProfile = new UserProfile();
        userProfile.setDateOfBirth(LocalDate.ofEpochDay(1L));
        userProfile.onCreate();
        assertEquals("nullnull", userProfile.getFullName());
    }

    /**
     * Method under test: {@link UserProfile#onUpdate()}
     */
    @Test
    void testOnUpdate() {
        CreateInfo createInfo = mock(CreateInfo.class);
        doNothing().when(createInfo).setUpdatedAtAd((LocalDateTime) any());
        LocalDate dateOfBirth = LocalDate.ofEpochDay(1L);
        Integer age = Integer.valueOf(1);
        (new UserProfile(123L, "Jane", "Middle Name", "Doe", "Dr Jane Doe", dateOfBirth, age, "42 Main St",
                "alice.liddell@example.org", new User(), createInfo)).onUpdate();
        verify(createInfo).setUpdatedAtAd((LocalDateTime) any());
    }
}

