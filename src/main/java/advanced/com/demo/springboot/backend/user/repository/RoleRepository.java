package advanced.com.demo.springboot.backend.user.repository;

import advanced.com.demo.springboot.backend.user.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Query("SELECT r FROM Role r WHERE CONCAT(r.name, ' ', r.id) LIKE %?1% ")
    Page<Role> search(String search,
                      Pageable pageable
    );

}