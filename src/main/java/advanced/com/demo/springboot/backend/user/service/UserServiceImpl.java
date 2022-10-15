package advanced.com.demo.springboot.backend.user.service;

import advanced.com.demo.springboot.backend.exception.CustomApiException;
import advanced.com.demo.springboot.backend.exception.CustomNotFoundException;
import advanced.com.demo.springboot.backend.helper.JSONResponseHelper;
import advanced.com.demo.springboot.backend.helper.email.EmailSender;
import advanced.com.demo.springboot.backend.helper.email.EmailTemplate;
import advanced.com.demo.springboot.backend.helper.email.EmailValidator;
import advanced.com.demo.springboot.backend.helper.token.ConfirmToken;
import advanced.com.demo.springboot.backend.helper.token.TokenService;
import advanced.com.demo.springboot.backend.user.DTO.UpdateUserDTO;
import advanced.com.demo.springboot.backend.user.model.Role;
import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.user.repository.RoleRepository;
import advanced.com.demo.springboot.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static advanced.com.demo.springboot.backend.helper.PaginateHelper.limitPageable;
import static advanced.com.demo.springboot.backend.helper.PaginateHelper.pageable;
import static advanced.com.demo.springboot.backend.helper.utils.UserCriteria.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@PropertySource("classpath:application-dev.properties")
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailSender emailSender;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private Environment env;

    /**
     * @param username String
     * @return Spring's userDetails
     * @throws UsernameNotFoundException Exception
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getByUsername(username)
                .orElseThrow(() -> new CustomApiException("user not found in the database"));
        if (user == null){
            log.error("user not found in database");
        }else{
            log.info("user found in the database {}", user.getUsername());
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Objects.requireNonNull(user).getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

    }

    /**
     * @param user Object
     * @return Saved User
     * @apiNote Save User Directly
     */
    @Override
    public User saveUser(User user) {
        log.info("saving user {} to the database", user.getUsername());
        return userRepository.save(user);
    }

    /**
     * @param user Object
     * @return CustomResponse
     */
    public Map<String, String> saveUserIntoDB(User user){

        User userDb = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmToken confirmToken = new ConfirmToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                userDb
        );

        tokenService.saveToken(confirmToken);
        String link = String.format("http://%s:%s/api/user-app/confirm-user?token=" + token,
                env.getProperty("server.address"), env.getProperty("server.port"));
        emailSender.send(user.getEmail(),
                EmailTemplate.buildRegisterMail(user.getUsername(), link));

        return JSONResponseHelper.CustomResponse("Confirmation Email Sent Successfully to your email");
    }


    /**
     * @param role Object
     * @return Role
     */
    @Override
    public Role saveUserRole(Role role) {
        log.info("saving role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    /**
     * @param user Object
     * @param role Object
     * @return Object
     */
    @Override
    public Object addRoleToUser(User user, Role role) {
        user.getRoles().add(role);
        userRepository.save(user);
        return JSONResponseHelper.CustomResponse("Roles Added");
    }

    /**
     * @param username String
     * @return UserObject
     */
    @Override
    public User getUser(String username) {
        log.info("getting user {} the database", username);
        return userRepository.getByUsername(username)
                .orElseThrow(() -> new CustomApiException("User with this username: " + username + " not found!"));
    }

    /**
     * @param offset int
     * @param limit int
     * @param sortBy String
     * @param search String
     * @param id int8
     * @param username String
     * @param email String
     * @return Paginated UserList
     */
    @Override
    public Page<User> getUsers(Integer offset,
                               Integer limit,
                               String sortBy,
                               String search,
                               Long id,
                               String username,
                               String email) {
        log.info("getting all users");
        Pageable pageable = pageable(offset, limit,sortBy);
        if(limit==0){
            return userRepository.findAll(limitPageable(this.getUsersCount(), sortBy));
        }
        return userRepository.findAll(Specification
                .where(idFilter(id))
                .and(usernameFilter(username))
                .and(emailFilter(email))
                .and(searchUser(search))
                , pageable);

    }

    /**
     * @param id int8
     * @return UserObject
     */
    public User getUserById(Long id) {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new CustomNotFoundException("User with this id: " + id + " not found"));
    }

    /**
     * @param username String
     * @return UserObject
     */
    public User getByUsername(String username) {
        return userRepository.getByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("User with this username: " + username + " not found!"));
    }

    /**
     * @param UserDB UserObject
     * @param userinfo UpdateDTO
     * @return updatedUser
     */
    @Override
    public User updateUser(User UserDB,
                           UpdateUserDTO userinfo){


        if(Objects.nonNull(userinfo.getUsername())
                && !"".equals(userinfo.getUsername()))
        {
            userRepository.getByUsername(userinfo.getUsername())
                    .orElseThrow(() -> new CustomApiException("User with this username: " + userinfo.getUsername() + " already exists!"));
            UserDB.setUsername(userinfo.getUsername());
        }

        if(Objects.nonNull(userinfo.getEmail())
                && !"".equals(userinfo.getEmail()))
        {
            userRepository.getUserByEmail(userinfo.getEmail())
                    .orElseThrow(() -> new CustomApiException("User with this email: " + userinfo.getEmail() + " already exists!"));
            boolean testEmail = EmailValidator.testEmailValidator(userinfo.getEmail());
            if(!testEmail){
                throw new CustomApiException("Please provide a valid email!");
            }
            UserDB.setEmail(userinfo.getEmail());
        }

        return userRepository.save(UserDB);
    }

    /**
     * @param user Object
     * @return CustomResponse
     */
    @Override
    public Object resetPassword(User user) {

        String token = UUID.randomUUID().toString();
        ConfirmToken confirmToken = new ConfirmToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user
        );
        tokenService.saveToken(confirmToken);
        String link = String.format("http://%s:%s/api/user-app/confirm-password?token=" + token,
                env.getProperty("server.address"), env.getProperty("server.port"));
        emailSender.send(user.getEmail(),
                EmailTemplate.buildResetPasswordMail(user.getUsername(), link));
        return JSONResponseHelper.CustomResponse("Password Reset Email Sent Successfully to your email");
    }

    /**
     * @param token String
     * @return CustomResponse
     */
    @Override
    public Object confirmUser(String token) {
        ConfirmToken confirmationToken = tokenService
                .getToken(token)
                .orElseThrow(() -> new CustomNotFoundException("token not found"));

        LocalDateTime expiredAt = confirmationToken.getExpires_at();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new CustomApiException("Token Expired");
        }

        tokenService.setConfirmedAt(token);
        User userDb = userRepository.getUserById(confirmationToken.getAppUser().getId())
                .orElseThrow(() -> new CustomNotFoundException("User not found!"));
        userDb.setIsActive(true);
        userRepository.save(userDb);
        return JSONResponseHelper.CustomResponse("User Successfully Verified, You can try logging in !");
    }

    /**
     * @return totalUserCount
     */
    @Override
    public int getUsersCount() {
        return userRepository.getUserCount();
    }

    /**
     * @param token String
     * @param newPassword String
     * @param confirmPassword String
     * @return CustomResponse
     */
    @Transactional
    public Object confirmPassword(String token, String newPassword, String confirmPassword){
        ConfirmToken confirmationToken = tokenService
                .getToken(token)
                .orElseThrow(() -> new CustomNotFoundException("token not found"));

        LocalDateTime expiredAt = confirmationToken.getExpires_at();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new CustomApiException("Token Expired");
        }

        if (!Objects.equals(newPassword, confirmPassword)){
            throw new CustomApiException("Password do not match");
        }

        tokenService.setConfirmedAt(token);

        User userDb = userRepository.getUserById(confirmationToken.getAppUser().getId())
                .orElseThrow(() -> new CustomNotFoundException("User not found!"));
        userDb.setPassword(passwordEncoder.encode(confirmPassword));

        userRepository.save(userDb);

        return JSONResponseHelper.CustomResponse("Password reset successfully!");
    }

    /**
     * @return Database configs from resource
     */
    public Boolean getDbConfig(){
        return Boolean.parseBoolean(env.getProperty("spring.flyway.enabled"));
    }
}
