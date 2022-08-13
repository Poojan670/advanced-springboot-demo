package advanced.com.demo.springboot.backend.userprofile.service;

import advanced.com.demo.springboot.backend.exception.CustomApiException;
import advanced.com.demo.springboot.backend.exception.CustomNotFoundException;
import advanced.com.demo.springboot.backend.helper.CustomOffsetPagination;
import advanced.com.demo.springboot.backend.helper.MediaHelper.FileUploadHelper;
import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.user.repository.UserRepository;
import advanced.com.demo.springboot.backend.userprofile.DTO.UserProfileDTO;
import advanced.com.demo.springboot.backend.userprofile.model.UserProfile;
import advanced.com.demo.springboot.backend.userprofile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final FileUploadHelper fileUploadHelper;
    private final UserRepository userRepository;
    @Override
    public UserProfile saveUserDetails(UserProfileDTO request) {
        User user = userRepository.getByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        )
                .orElseThrow(()-> new CustomApiException("User not found!"));
        String photo = "";
        if (request.getPhoto() == null) {
            photo = null;
        } else {
            photo = fileUploadHelper.uploadFile("image", request.getPhoto().getName(),
                    request.getPhoto());
        }

        UserProfile userProfile = UserProfile.builder()
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .photo(photo)
                .user(user)
                .build();

        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile updateUserDetails(
            String firstName, String middleName, String lastName,
            String address, LocalDate dateOfBirth, MultipartFile photo
    ) {
        User user = userRepository.getByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        )
                .orElseThrow(() -> new CustomApiException("User not found"));
        UserProfile userProfileDb = userProfileRepository.getUserProfileByUser(user)
                .orElseThrow(() -> new CustomNotFoundException("User Details not found"));

        if(Objects.nonNull(firstName) && !"".equals(firstName)){
            userProfileDb.setFirstName(firstName);
        }
        if(Objects.nonNull(middleName) && !"".equals(middleName)){
            userProfileDb.setMiddleName(middleName);
        }
        if(Objects.nonNull(lastName) && !"".equals(lastName)){
            userProfileDb.setLastName(lastName);
        }
        if(Objects.nonNull(address) && !"".equals(address)){
            userProfileDb.setAddress(address);
        }
        if(Objects.nonNull(dateOfBirth)){
            userProfileDb.setDateOfBirth(dateOfBirth);
            userProfileDb.setAge(
                    LocalDate.now().getYear() - dateOfBirth.getYear()
            );
        }
        String photo_name = "";
        if (photo == null) {
            photo_name = userProfileDb.getPhoto();
        } else {
            photo_name = fileUploadHelper.uploadFile("image", photo.getName(),
                    photo);
        }
        userProfileDb.setPhoto(photo_name);
        return userProfileRepository.save(userProfileDb);
    }

    @Override
    public UserProfile getUserDetail(Long id) {
        UserProfile userProfile =  userProfileRepository.getUserDetailsById(id)
                .orElseThrow(() -> new CustomNotFoundException("User Details with this id: " + id + " not found"));
        UserProfile testDetail = userProfileRepository.getUserProfileByUser(
                userRepository.getByUsername(
                        (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                )
                        .orElseThrow(() -> new CustomApiException("User not found!"))
        )
                .orElseThrow(() -> new CustomApiException("User Details not Found"));
        if(testDetail.getUser().getRoles().contains("ADMIN") || userProfile == testDetail){
            return userProfile;
        }else{
            throw new CustomApiException("You are not allowed!");
        }
    }

    @Override
    public void deleteUserDetail(Long id) {
        userProfileRepository.delete(this.getUserDetail(id));
    }

    @Override
    public Page<UserProfile> getUserDetails(Integer offset, Integer limit, String sortBy, String search) {
            Pageable pageable;
            if (sortBy.startsWith("-")){
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy.substring(1)).descending());
            }else{
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy));
            }
            return userProfileRepository.findAll(pageable);
        }
}
