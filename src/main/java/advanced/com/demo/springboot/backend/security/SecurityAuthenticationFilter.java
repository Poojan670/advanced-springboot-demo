package advanced.com.demo.springboot.backend.security;


import advanced.com.demo.springboot.backend.exception.CustomApiException;
import advanced.com.demo.springboot.backend.user.DTO.LoginDTO;
import advanced.com.demo.springboot.backend.user.repository.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class SecurityAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;


    public SecurityAuthenticationFilter(AuthenticationManager authenticationManager,
                                        UserRepository userRepository){
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            String body = request.getReader().lines().collect(Collectors.joining());
            LoginDTO userLogin = new ObjectMapper().readValue(body, LoginDTO.class);
            String username = userLogin.getUsername();
            String password = userLogin.getPassword();

            advanced.com.demo.springboot.backend.user.model.User user = userRepository.getByUsername(username)
                    .orElseThrow(() -> new CustomApiException("User with this username not found!"));
            boolean active = user.getIsActive();
            if(!active){
                throw new CustomApiException("User Not Verified !!!");
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication
    ) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        log.info("time: {}", System.currentTimeMillis());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+ 720 * 60 * 10000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);

        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1440 * 60 * 10000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        String username = user.getUsername();
        Collection<GrantedAuthority> roles = user.getAuthorities();

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> tokens = new HashMap<>();

        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        result.put("tokens", tokens);

        result.put("username", username);
        result.put("roles", roles);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),result);
    }
}