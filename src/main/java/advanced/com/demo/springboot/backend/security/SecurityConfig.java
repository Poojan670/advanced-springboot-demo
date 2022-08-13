package advanced.com.demo.springboot.backend.security;

import advanced.com.demo.springboot.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;


@Configuration @EnableWebSecurity @RequiredArgsConstructor
public class SecurityConfig  extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        SecurityAuthenticationFilter securityAuthenticationFilter = new SecurityAuthenticationFilter(authenticationManagerBean(),
                userRepository);

        http.cors().and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll();

        securityAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**").permitAll();
        http.authorizeRequests().antMatchers( "/api/user-app/register/**").permitAll();
        http.authorizeRequests().antMatchers( "/api/docs/**").permitAll();
        http.authorizeRequests().antMatchers( "/api/swagger-ui/**").permitAll();
        http.authorizeRequests().antMatchers( "/api/core-app/getImage/**").permitAll();
        http.authorizeRequests().antMatchers( "/api/core-app/getFile/**").permitAll();
        http.authorizeRequests().antMatchers( "/api/user-app/reset-password/**").permitAll();
        http.authorizeRequests().antMatchers( "/api/user-app/confirm-password/**").permitAll();
        http.authorizeRequests().antMatchers( "/api/user-app/confirm-user/**").permitAll();

        // Role-Wise Authorization
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user-app/users/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/user-app/create-user-role/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(securityAuthenticationFilter);
        http.addFilterBefore(new SecurityAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}

