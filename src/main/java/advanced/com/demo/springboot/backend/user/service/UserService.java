package advanced.com.demo.springboot.backend.user.service;

import advanced.com.demo.springboot.backend.user.DTO.UpdateUserDTO;
import advanced.com.demo.springboot.backend.user.model.Role;
import advanced.com.demo.springboot.backend.user.model.User;
import org.springframework.data.domain.Page;

public interface UserService {
    User saveUser(User user);

    Role saveUserRole(Role role);

    Object addRoleToUser(User user, Role role);

    User getUser(String username);

    Page<User> getUsers(Integer offset,
                        Integer limit,
                        String sortBy,
                        String search,
                        Long id,
                        String username,
                        String email);

    User updateUser(User UserDB,
                    UpdateUserDTO userinfo);

    Object resetPassword(User user);

    Object confirmUser(String token);

    int getUsersCount();
}


