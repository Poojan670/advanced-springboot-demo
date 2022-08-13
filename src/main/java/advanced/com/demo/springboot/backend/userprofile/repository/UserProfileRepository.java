package advanced.com.demo.springboot.backend.userprofile.repository;

import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.userprofile.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    UserProfile getUserDetailsById(Long id );

    UserProfile getUserProfileByUser(User user);

}
