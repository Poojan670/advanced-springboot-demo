package advanced.com.demo.springboot.backend.user.controller;

import advanced.com.demo.springboot.backend.core.model.CreateInfo;
import advanced.com.demo.springboot.backend.user.DTO.*;
import advanced.com.demo.springboot.backend.user.model.Role;
import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.user.repository.RoleRepository;
import advanced.com.demo.springboot.backend.user.repository.UserRepository;
import advanced.com.demo.springboot.backend.user.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {UserController.class})
@ExtendWith(SpringExtension.class)
class UserControllerTest {
    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserServiceImpl userServiceImpl;

    /**
     * Method under test: {@link UserController#getUsers(Integer, Integer, String, String, Long, String, String)}
     */
    @Test
    void testGetUsers() throws Exception {
        when(userServiceImpl.getUsers((Integer) any(), (Integer) any(), (String) any(), (String) any(), (Long) any(),
                (String) any(), (String) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        MockHttpServletRequestBuilder paramResult = MockMvcRequestBuilders.get("/api/user-app/users")
                .param("email", "foo");
        MockHttpServletRequestBuilder paramResult1 = paramResult.param("id", String.valueOf(1L));
        MockHttpServletRequestBuilder paramResult2 = paramResult1.param("limit", String.valueOf(1));
        MockHttpServletRequestBuilder requestBuilder = paramResult2.param("offset", String.valueOf(1))
                .param("search", "foo")
                .param("sortBy", "foo")
                .param("username", "foo");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }

    /**
     * Method under test: {@link UserController#confirmUser(String)}
     */
    @Test
    void testConfirmUser() throws Exception {
        when(userServiceImpl.confirmUser((String) any())).thenReturn("Confirm User");
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/user-app/confirm-user")
                .param("token", "foo");
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Confirm User"));
    }

    /**
     * Method under test: {@link UserController#saveUserRole(String)}
     */
    @Test
    void testSaveUserRole() throws Exception {
        CreateInfo createInfo = new CreateInfo();
        createInfo.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        Role role = new Role();
        role.setCreateInfo(createInfo);
        role.setId(123L);
        role.setName("Name");
        when(userServiceImpl.saveUserRole((Role) any())).thenReturn(role);
        MockHttpServletRequestBuilder contentTypeResult = MockMvcRequestBuilders.post("/api/user-app/create-user-role")
                .contentType(MediaType.APPLICATION_JSON);

        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpServletRequestBuilder requestBuilder = contentTypeResult
                .content(objectMapper.writeValueAsString(new String()));
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":123,\"name\":\"Name\",\"createInfo\":{\"createdAtAd\":[1,1,1,1,1],\"updatedAtAd\":[1,1,1,1,1]}}"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/api/user-app/create-user-role"));
    }

    /**
     * Method under test: {@link UserController#updateUser(Long, UpdateUserDTO)}
     */
    @Test
    void testUpdateUser() throws Exception {
        CreateInfo createInfo = new CreateInfo();
        createInfo.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        User user = new User();
        user.setCreateInfo(createInfo);
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setIsActive(true);
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");

        CreateInfo createInfo1 = new CreateInfo();
        createInfo1.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo1.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        User user1 = new User();
        user1.setCreateInfo(createInfo1);
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setIsActive(true);
        user1.setPassword("iloveyou");
        user1.setRoles(new ArrayList<>());
        user1.setUsername("janedoe");
        when(userServiceImpl.getUserById((Long) any())).thenReturn(user);
        when(userServiceImpl.updateUser((User) any(), (UpdateUserDTO) any())).thenReturn(user1);

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setEmail("jane.doe@example.org");
        updateUserDTO.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(updateUserDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/api/user-app/user/{id}", 123L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"));
    }


    /**
     * Method under test: {@link UserController#resetPassword(ResetPasswordDTO)}
     */
    @Test
    void testResetPassword() throws Exception {
        when(userServiceImpl.resetPassword((User) any())).thenReturn("Reset Password");

        CreateInfo createInfo = new CreateInfo();
        createInfo.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        User user = new User();
        user.setCreateInfo(createInfo);
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setIsActive(true);
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.getUserByEmail((String) any())).thenReturn(ofResult);

        ResetPasswordDTO resetPasswordDTO = new ResetPasswordDTO();
        resetPasswordDTO.setEmail("jane.doe@example.org");
        String content = (new ObjectMapper()).writeValueAsString(resetPasswordDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user-app/reset-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Reset Password"));
    }

    /**
     * Method under test: {@link UserController#addUserRole(AddUserRoleDTO)}
     */
    @Test
    void testAddUserRole() throws Exception {
        when(userServiceImpl.addRoleToUser((User) any(), (Role) any())).thenReturn("Add Role To User");

        CreateInfo createInfo = new CreateInfo();
        createInfo.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        User user = new User();
        user.setCreateInfo(createInfo);
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setIsActive(true);
        user.setPassword("iloveyou");
        user.setRoles(new ArrayList<>());
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.getByUsername((String) any())).thenReturn(ofResult);

        CreateInfo createInfo1 = new CreateInfo();
        createInfo1.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo1.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        Role role = new Role();
        role.setCreateInfo(createInfo1);
        role.setId(123L);
        role.setName("Name");
        Optional<Role> ofResult1 = Optional.of(role);
        when(roleRepository.findByName((String) any())).thenReturn(ofResult1);

        AddUserRoleDTO addUserRoleDTO = new AddUserRoleDTO();
        addUserRoleDTO.setRoleName("Role Name");
        addUserRoleDTO.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(addUserRoleDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user-app/add-user-role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Add Role To User"));
    }

    /**
     * Method under test: {@link UserController#confirmPassword(String, ConfirmPasswordDTO)}
     */
    @Test
    void testConfirmPassword() throws Exception {
        when(userServiceImpl.confirmPassword((String) any(), (String) any(), (String) any()))
                .thenReturn("Confirm Password");

        ConfirmPasswordDTO confirmPasswordDTO = new ConfirmPasswordDTO();
        confirmPasswordDTO.setConfirmPassword("iloveyou");
        confirmPasswordDTO.setNewPassword("iloveyou");
        String content = (new ObjectMapper()).writeValueAsString(confirmPasswordDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user-app/confirm-password")
                .param("token", "foo")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("text/plain;charset=ISO-8859-1"))
                .andExpect(MockMvcResultMatchers.content().string("Confirm Password"));
    }

    /**
     * Method under test: {@link UserController#getUser()}
     */
    @Test
    void testGetUser() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Method under test: {@link UserController#saveUser(SaveUserDTO)}
     */
    @Test
    void testSaveUser() throws Exception {
        when(userServiceImpl.saveUserIntoDB((User) any())).thenReturn(new HashMap<>());
        when(userRepository.getUserByEmail((String) any())).thenReturn(Optional.empty());

        CreateInfo createInfo = new CreateInfo();
        createInfo.setCreatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));
        createInfo.setUpdatedAtAd(LocalDateTime.of(1, 1, 1, 1, 1));

        Role role = new Role();
        role.setCreateInfo(createInfo);
        role.setId(123L);
        role.setName("Name");
        Optional<Role> ofResult = Optional.of(role);
        when(roleRepository.findByName((String) any())).thenReturn(ofResult);
        when(passwordEncoder.encode((CharSequence) any())).thenReturn("secret");

        SaveUserDTO saveUserDTO = new SaveUserDTO();
        saveUserDTO.setEmail("jane.doe@example.org");
        saveUserDTO.setPassword("iloveyou");
        saveUserDTO.setUsername("janedoe");
        String content = (new ObjectMapper()).writeValueAsString(saveUserDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/user-app/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(userController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{}"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("http://localhost/api/user-app/create-user"));
    }
}

