package advanced.com.demo.springboot.backend.user;

import advanced.com.demo.springboot.backend.user.model.User;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserCriteria {
    public static Specification<User> specification = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

    public static Specification<User> idFilter(Long id){
        if(Objects.nonNull(id)){
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
        }else{
            return specification;
        }
    }

    public static Specification<User> usernameFilter(String username){
        if(Objects.nonNull(username) && !Objects.equals(username, "")){
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("username"), username);
        }else{
            return specification;
        }
    }

    public static Specification<User> emailFilter(String email){
        if(Objects.nonNull(email) && !Objects.equals(email, "")){
            return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("email"), email);
        }else{
            return specification;
        }
    }

    public static Specification<User> searchUser(String search) {

        if (Objects.nonNull(search) && !Objects.equals(search, "")) {
            specification = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.upper(root.get("username")),
                                "%" + search.toUpperCase() + "%")
                );

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.upper(root.get("email")),
                                "%" + search.toUpperCase() + "%")
                );
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
        }else{
            return specification;
        }
        return specification;
    }

}
