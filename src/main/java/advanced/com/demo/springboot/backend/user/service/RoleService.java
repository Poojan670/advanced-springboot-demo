package advanced.com.demo.springboot.backend.user.service;

import advanced.com.demo.springboot.backend.user.model.Role;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface RoleService {

    Optional<Role> getRoleById(Long id );
    Role getRoleByName(String name);
    Role saveRole(Role role);
    Page<Role> getRoles(Integer offset,
                        Integer limit,
                        String sortBy,
                        String search);

}
