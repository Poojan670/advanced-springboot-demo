package advanced.com.demo.springboot.backend.user.DTO;

import lombok.Data;

@Data
public class SaveUserDTO {
    private String username;
    private String email;
    private String password;
}
