package advanced.com.demo.springboot.backend.user.controller;

import advanced.com.demo.springboot.backend.exception.CustomApiException;
import advanced.com.demo.springboot.backend.exception.CustomNotFoundException;
import advanced.com.demo.springboot.backend.user.DTO.*;
import advanced.com.demo.springboot.backend.helper.email.EmailValidator;
import advanced.com.demo.springboot.backend.user.model.Role;
import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.user.repository.RoleRepository;
import advanced.com.demo.springboot.backend.user.repository.UserRepository;
import advanced.com.demo.springboot.backend.user.service.UserServiceImpl;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-app")
@Slf4j
public class UserController {
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "0") Integer offset,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") Map<String, String> filter
    ){
        var id = filter.getOrDefault("id", "");
        var username = filter.getOrDefault("username", "");
        var email = filter.getOrDefault("email", "");
        return ResponseEntity.ok().body(userService.getUsers(
                offset, limit, sortBy, search,
                id, username, email
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> saveUser(@Valid @RequestBody SaveUserDTO request){
        Role role = roleRepository.findByName("USER");
        boolean testEmail = EmailValidator.testEmailValidator(request.getEmail());
        if(!testEmail){throw new CustomApiException("Please provide a valid email!");}
        if (userRepository.getUserByEmail(request.getEmail()) != null) {
            throw new CustomApiException("Email Already Exists!");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(false);
        user.getRoles().add(role);

        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user-app/create-user").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUserIntoDB(user));
    }

    @GetMapping(value = "/confirm-user")
    public ResponseEntity<Object> confirmUser(@RequestParam String token){
        return ResponseEntity.ok().body(userService.confirmUser(token));
    }

    @PostMapping("/create-user-role")
    public ResponseEntity<Role> saveUserRole(@Valid @RequestBody String name){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user-app/create-user-role").toUriString());
        Role role = new Role();
        role.setName(name);
        return ResponseEntity.created(uri).body(userService.saveUserRole(role));
    }
    @PostMapping("/add-user-role")
    public ResponseEntity<Object> addUserRole(@RequestBody AddUserRoleDTO reqData){
        User user  = userRepository.getByUsername(reqData.getUsername());
        Role role = roleRepository.findByName(reqData.getRoleName());
        if(user==null || role==null){
            throw new CustomApiException("User/Role not found, Please Try Again");
        }
        Object resp = userService.addRoleToUser(user, role);
        return ResponseEntity.ok().body(resp);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(){
        User user = userRepository.getByUsername((String)
                SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return ResponseEntity.ok().body(user);
    }

    @PatchMapping(value = "user/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ResponseEntity<Object> updateUser(@PathVariable Long id,
                                             @RequestBody UpdateUserDTO userinfo){
        User UserDB = userService.getUserById(id);
        return ResponseEntity.ok().body(userService.updateUser(UserDB,userinfo));
    }

    @PostMapping(value = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO){
        User user = userRepository.getUserByEmail(resetPasswordDTO.getEmail());
        if(user==null){
            throw new CustomNotFoundException("User with this email :" + resetPasswordDTO.getEmail() +" not found!");
        }
        return ResponseEntity.ok().body(userService.resetPassword(user));
    }

    @PostMapping(value = "/confirm-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> confirmPassword(@RequestParam String token,
                                                  @RequestBody ConfirmPasswordDTO request){
        return ResponseEntity.ok().body(userService.confirmPassword(token,
                request.getNewPassword(), request.getConfirmPassword()));
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws CustomApiException,
            IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier jwtVerifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = jwtVerifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                System.out.println(username);
                User user = userService.getUser(username);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis()+ 100 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                System.out.println(access_token);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception){

                log.error("error logging in {}", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else{
            throw new CustomApiException("refresh token is missing");
        }
    }
}
