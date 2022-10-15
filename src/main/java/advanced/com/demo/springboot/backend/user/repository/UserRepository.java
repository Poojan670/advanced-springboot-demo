package advanced.com.demo.springboot.backend.user.repository;

import advanced.com.demo.springboot.backend.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> getByUsername(String username);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);

    @Query("select coalesce(count(id), 0) from User ")
    int getUserCount();

}
