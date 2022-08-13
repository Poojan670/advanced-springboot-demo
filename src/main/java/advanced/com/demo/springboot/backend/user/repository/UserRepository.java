package advanced.com.demo.springboot.backend.user.repository;

import advanced.com.demo.springboot.backend.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);
    User getUserById(Long id);

    @Query("SELECT u FROM User u WHERE CONCAT(u.username, ' ', u.id, u.email) LIKE %?1% ")
    Page<User> search(String search,
                      Pageable pageable
    );

    User getUserByEmail(String email);

    @Query("select u from User u where u.id=?1  or u.username=?2 or u.email=?3")
    Page<User> filter(Long id,
                      String username,
                      String email,
                      Pageable pageable);

}
