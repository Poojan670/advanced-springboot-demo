package advanced.com.demo.springboot.backend.user.DTO;

import lombok.Data;

@Data
public class ConfirmPasswordDTO {
    private String newPassword;
    private String confirmPassword;
}
