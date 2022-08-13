package advanced.com.demo.springboot.backend.user.service;

import advanced.com.demo.springboot.backend.exception.CustomApiException;
import advanced.com.demo.springboot.backend.exception.CustomNotFoundException;
import advanced.com.demo.springboot.backend.helper.CustomOffsetPagination;
import advanced.com.demo.springboot.backend.helper.JSONResponseHelper;
import advanced.com.demo.springboot.backend.helper.email.EmailSender;
import advanced.com.demo.springboot.backend.helper.email.EmailTemplate;
import advanced.com.demo.springboot.backend.helper.email.EmailValidator;
import advanced.com.demo.springboot.backend.user.DTO.UpdateUserDTO;
import advanced.com.demo.springboot.backend.user.model.Role;
import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.user.repository.RoleRepository;
import advanced.com.demo.springboot.backend.user.repository.UserRepository;
import advanced.com.demo.springboot.backend.helper.token.ConfirmToken;
import advanced.com.demo.springboot.backend.helper.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

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
    @Override
    public User saveUser(User user) {
        log.info("saving user {} to the database", user.getUsername());
        return userRepository.save(user);
    }

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


    @Override
    public Role saveUserRole(Role role) {
        log.info("saving role {} to the database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public Object addRoleToUser(User user, Role role) {
        user.getRoles().add(role);
        userRepository.save(user);
        return JSONResponseHelper.CustomResponse("Roles Added");
    }

    @Override
    public User getUser(String username) {
        log.info("getting user {} the database", username);
        return userRepository.getByUsername(username)
                .orElseThrow(() -> new CustomApiException("User with this username: " + username + " not found!"));
    }

    @Override
    public Page<User> getUsers(Integer offset,Integer limit, String sortBy,
                               String search,
                               String id,String username,String email) {
        log.info("getting all users");
        if (!id.equals("") || !username.equals("") || !email.equals("")) {
            Pageable pageable;
            if (sortBy.startsWith("-")) {
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy.substring(1)).descending());
            } else {
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy));
            }
            if (id.equals("") && username.equals("")) {
                return userRepository.filter(null, null, email, pageable);
            } else if (id.equals("") && email.equals("")) {
                return userRepository.filter(null, username, null, pageable);
            } else if (username.equals("") && email.equals("")) {
                return userRepository.filter(Long.valueOf(id), null, null, pageable);
            } else {
                return userRepository.filter(Long.valueOf(id), username, email, pageable);
            }
        }
        else if(search==null || search.equals("")){
            Pageable pageable;
            if (sortBy.startsWith("-")){
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy.substring(1)).descending());
            }else{
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy));
            }
            return userRepository.findAll(pageable);
        }else{
            Pageable pageable;
            if (sortBy.startsWith("-")){
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy.substring(1)).descending());
            }else{
                pageable = new CustomOffsetPagination(offset, limit, Sort.by(sortBy));
            }
            return userRepository.search(search, pageable);
        }
    }

    public User getUserById(Long id) {
        return userRepository.getUserById(id)
                .orElseThrow(() -> new CustomNotFoundException("User with this id: " + id + " not found"));
    }

    public User getByUsername(String username) {
        return userRepository.getByUsername(username)
                .orElseThrow(() -> new CustomNotFoundException("User with this username: " + username + " not found!"));
    }

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
            User user =  userRepository.getUserByEmail(userinfo.getEmail())
                    .orElseThrow(() -> new CustomApiException("User with this email: " + userinfo.getEmail() + " already exists!"));
            boolean testEmail = EmailValidator.testEmailValidator(userinfo.getEmail());
            if(!testEmail){
                throw new CustomApiException("Please provide a valid email!");
            }
            UserDB.setEmail(userinfo.getEmail());
        }

        return userRepository.save(UserDB);
    }

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

    public Boolean getDbConfig(){
        return Boolean.parseBoolean(env.getProperty("spring.flyway.enabled"));
    }
}
