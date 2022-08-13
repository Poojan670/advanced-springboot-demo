package advanced.com.demo.springboot.backend;

import advanced.com.demo.springboot.backend.user.model.Role;
import advanced.com.demo.springboot.backend.user.model.User;
import advanced.com.demo.springboot.backend.user.service.RoleServiceImpl;
import advanced.com.demo.springboot.backend.user.service.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping("*")
						.allowedOrigins("http://localhost:3000", "http://localhost:3001")
						.allowedMethods("*")
						.allowedHeaders("*")
						.allowedOrigins("*")
						.allowCredentials(true);
			}
		};
	}

	@Configuration
	public class Config implements WebMvcConfigurer {
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**").allowedOrigins("*");
		}
	}

	// If Hibernate
	@Bean
	CommandLineRunner run(RoleServiceImpl roleService,
						  UserServiceImpl userService) {

		return args -> {

			if (!userService.getDbConfig()) {
				try {
					roleService.saveRole(new Role(1L, "ADMIN", null));
					roleService.saveRole(new Role(2L, "MANAGER", null));
					roleService.saveRole(new Role(3L, "ASSISTANT", null));
					roleService.saveRole(new Role(4L, "USER", null));
					System.out.println("Roles Successfully Created!");
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("Roles Already Created!");
				}

				try {
					User user = User.builder()
							.username("admin")
							.email("po0janhunt@gmail.com")
							.password(bCryptPasswordEncoder().encode("admin"))
							.isActive(true)
							.build();
					userService.saveUser(user);
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println("Admin User Already Created!");
				}

				try {
					var checkUser = userService.getByUsername("admin").getRoles();
					if (checkUser.isEmpty()) {
						User user = userService.getByUsername("admin");
						Role role = roleService.getRoleByName("ADMIN");
						userService.addRoleToUser(user, role);
					}
					System.out.println("Admin role added to superuser");
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println(e.getClass());
					System.out.println("Admin role already added");
				}
			}
		};
	}
}
