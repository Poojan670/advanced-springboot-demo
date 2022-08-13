package advanced.com.demo.springboot.backend.user.model;

import advanced.com.demo.springboot.backend.core.model.CreateInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty(message = "Role Name is required!")
    @Column(unique = true)
    private String name;

    @Embedded
    private CreateInfo createInfo;
    @PrePersist
    protected void onCreate() throws Exception {
        final var data = new CreateInfo();
        data.setCreatedAtAd(LocalDateTime.now());
        this.createInfo = data;
    }
    @PreUpdate
    protected void onUpdate(){
        createInfo.setUpdatedAtAd(LocalDateTime.now());
    }

}