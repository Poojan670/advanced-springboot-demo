package advanced.com.demo.springboot.backend.user.model;

import advanced.com.demo.springboot.backend.core.model.CreateInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @SequenceGenerator(
            name="user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Username is required")
    @NotNull
    private String username;

    @Column(unique = true)
    @NotBlank(message = "Email is required")
    @NotNull
    private String email;

    @JsonIgnore
    @NotBlank(message = "Password is required")
    private String password;

    @JsonIgnoreProperties({"createInfo"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles")
    private Collection<Role> roles = new ArrayList<>();

    private Boolean isActive = false;

    @Embedded
    private CreateInfo createInfo;
    @PrePersist
    protected void onCreate() {
        final var data = new CreateInfo();
        data.setCreatedAtAd(LocalDateTime.now());
        this.createInfo = data;
    }
    @PreUpdate
    protected void onUpdate(){
        createInfo.setUpdatedAtAd(LocalDateTime.now());
    }
}
