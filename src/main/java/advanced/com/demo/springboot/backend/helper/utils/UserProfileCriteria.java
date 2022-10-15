package advanced.com.demo.springboot.backend.helper.utils;

import advanced.com.demo.springboot.backend.userprofile.model.UserProfile;
import org.springframework.data.jpa.domain.Specification;

import java.util.Objects;

public class UserProfileCriteria {
    public static Specification<UserProfile> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

    public static Specification<UserProfile> idFilter(Long id){
        if(Objects.nonNull(id)){
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
        }else{
            return specification;
        }
    }

    public static Specification<UserProfile> userIdFilter(Long userId){
        if(Objects.nonNull(userId)){
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
        }else{
            return specification;
        }
    }

    public static Specification<UserProfile> searchUserProfile(String search) {

        if (Objects.nonNull(search) && !Objects.equals(search, "")) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                    criteriaBuilder.upper(root.get("user").get("username")),
                    "%" + search.toUpperCase() + "%");
        } else {
            return specification;
        }
    }
}
