package advanced.com.demo.springboot.backend.userprofile.controller;

import advanced.com.demo.springboot.backend.core.model.CreateInfo;
import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.userprofile.DTO.UpdateProfileDTO;
import advanced.com.demo.springboot.backend.userprofile.model.UserProfile;
import advanced.com.demo.springboot.backend.userprofile.service.UserProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UserProfileController.class})
@ExtendWith(SpringExtension.class)
class UserProfileControllerTest {
    @Autowired
    private UserProfileController userProfileController;

    @MockBean
    private UserProfileServiceImpl userProfileServiceImpl;


    /**
     * Method under test: {@link UserProfileController#getUserDetails(Integer, Integer, String, String, Long, Long)}
     */
    @Test
    void testGetUserDetails() throws Exception {
        when(userProfileServiceImpl.getUserDetails((Integer) any(), (Integer) any(), (String) any(), (String) any(),
                (Long) any(), (Long) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/api/user-details-app/user-details");
        MockHttpServletRequestBuilder paramResult = getResult.param("id", String.valueOf(1L));
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("limit", String.valueOf(1));
        MockHttpServletRequestBuilder paramResult2 = paramResult1.param("offset", String.valueOf(1))
                .param("search", "foo")
                .param("sortBy", "foo");
        MockHttpServletRequestBuilder requestBuilder = paramResult2.param("userId", String.valueOf(1L));
        MockMvcBuilders.standaloneSetup(userProfileController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }


    /**
     * Method under test: {@link UserProfileController#getUserDetail(Long)}
     */
    @Test
    void testGetUserDetail() throws Exception {
        CreateInfo createInfo = new CreateInfo();
        createInfo.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        CreateInfo createInfo1 = new CreateInfo();
        createInfo1.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo1.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        User user = new User();
        user.setCreateInfo(createInfo1);
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setIsActive(true);
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");

        UserProfile userProfile = new UserProfile();
        userProfile.setAddress("42 Main St");
        userProfile.setAge(Integer.valueOf(1));
        userProfile.setCreateInfo(createInfo);
        userProfile.setDateOfBirth(LocalDate.ofEpochDay(1L));
        userProfile.setFirstName("Jane");
        userProfile.setFullName("Dr Jane Doe");
        userProfile.setId(123L);
        userProfile.setLastName("Doe");
        userProfile.setMiddleName("Middle Name");
        userProfile.setPhoto("alice.liddell@example.org");
        userProfile.setUser(user);
        when(userProfileServiceImpl.getUserDetail((Long) any())).thenReturn(userProfile);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/user-details-app/user-detail/{id}", 123L);
        MockMvcBuilders.standaloneSetup(userProfileController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link UserProfileController#deleteUserDetail(Long)}
     */
    @Test
    void testDeleteUserDetail() throws Exception {
        doNothing().when(userProfileServiceImpl).deleteUserDetail((Long) any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/user-details-app/user-detail/{id}", 123L);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userProfileController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link UserProfileController#updateUserDetails(UpdateProfileDTO)}
     */
    @Test
    void testUpdateUserDetails() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/user-details-app/");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userProfileController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(415));
    }
}

