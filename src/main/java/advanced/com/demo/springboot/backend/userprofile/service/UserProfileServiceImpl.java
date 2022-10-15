package advanced.com.demo.springboot.backend.userprofile.service;

import advanced.com.demo.springboot.backend.exception.CustomApiException;
import advanced.com.demo.springboot.backend.exception.CustomNotFoundException;
import advanced.com.demo.springboot.backend.helper.MediaHelper.FileUploadHelper;
import advanced.com.demo.springboot.backend.helper.ModelMapperHelper;
import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.user.repository.UserRepository;
import advanced.com.demo.springboot.backend.userprofile.DTO.UpdateProfileDTO;
import advanced.com.demo.springboot.backend.userprofile.DTO.UserProfileDTO;
import advanced.com.demo.springboot.backend.userprofile.model.UserProfile;
import advanced.com.demo.springboot.backend.userprofile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;

import static advanced.com.demo.springboot.backend.helper.PaginateHelper.limitPageable;
import static advanced.com.demo.springboot.backend.helper.PaginateHelper.pageable;
import static advanced.com.demo.springboot.backend.helper.utils.UserProfileCriteria.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final FileUploadHelper fileUploadHelper;
    private final UserRepository userRepository;
    private EntityManager entityManager;

    /**
     * @param request UserProfileDTO
     * @return UserProfile
     */
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

    /**
     * @param updateProfileDTO requestObject
     * @return UpdatedUserProfile
     */
    @Override
    public UserProfile updateUserDetails(UpdateProfileDTO updateProfileDTO) {
        User user = userRepository.getByUsername(
                (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        )
                .orElseThrow(() -> new CustomApiException("User not found"));
        UserProfile userProfileDb = userProfileRepository.getUserProfileByUser(user)
                .orElseThrow(() -> new CustomNotFoundException("User Details not found"));

        String photo_name = "";
        if (updateProfileDTO.getPhoto() == null) {
            photo_name = userProfileDb.getPhoto();
        } else {
            photo_name = fileUploadHelper.uploadFile("image", updateProfileDTO.getPhoto().getName(),
                    updateProfileDTO.getPhoto());
        }
        userProfileDb.setPhoto(photo_name);
        ModelMapperHelper.mapperHelMapper().map(updateProfileDTO, userProfileDb);
        return userProfileRepository.save(userProfileDb);
    }

    /**
     * @param id int8
     * @return UserProfile
     */
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

    /**
     * @param id int8
     */
    @Override
    public void deleteUserDetail(Long id) {
        userProfileRepository.delete(this.getUserDetail(id));
    }

    public Integer getUserDetailsCount(){
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(UserProfile.class)));
        return entityManager.createQuery(criteriaQuery).getSingleResult().intValue();
    }

    /**
     * @param offset int
     * @param limit int
     * @param sortBy String
     * @param search String
     * @param id int8
     * @param userId int8
     * @return Paginated ProfileList
     */
    @Override
    public Page<UserProfile> getUserDetails(Integer offset,
                                            Integer limit,
                                            String sortBy,
                                            String search,
                                            Long id,
                                            Long userId) {
            Pageable pageable = pageable(offset,limit,sortBy);
            if(limit==0){
                return userProfileRepository
                        .findAll(limitPageable(this.getUserDetailsCount(), sortBy));
            }
            return userProfileRepository.findAll(Specification
                    .where(idFilter(id))
                    .and(userIdFilter(userId))
                    .and(searchUserProfile(search)), pageable);
        }
}
