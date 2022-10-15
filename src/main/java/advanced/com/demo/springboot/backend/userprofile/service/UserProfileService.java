package advanced.com.demo.springboot.backend.userprofile.service;

import advanced.com.demo.springboot.backend.userprofile.DTO.UpdateProfileDTO;
import advanced.com.demo.springboot.backend.userprofile.DTO.UserProfileDTO;
import advanced.com.demo.springboot.backend.userprofile.model.UserProfile;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public interface UserProfileService {

    UserProfile saveUserDetails(UserProfileDTO request);

    UserProfile updateUserDetails(
            UpdateProfileDTO updateProfileDTO
    );

    UserProfile getUserDetail(Long id);

    void deleteUserDetail(Long id);

    Page<UserProfile> getUserDetails(Integer offset,
                                     Integer limit,
                                     String sortBy,
                                     String search);
}
