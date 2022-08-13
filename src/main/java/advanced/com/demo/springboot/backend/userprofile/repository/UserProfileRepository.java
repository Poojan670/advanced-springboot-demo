package advanced.com.demo.springboot.backend.userprofile.repository;

import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.userprofile.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> getUserDetailsById(Long id );

    Optional<UserProfile> getUserProfileByUser(User user);

}
