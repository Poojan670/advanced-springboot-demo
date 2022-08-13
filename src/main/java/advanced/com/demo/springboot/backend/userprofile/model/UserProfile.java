package advanced.com.demo.springboot.backend.userprofile.model;

import advanced.com.demo.springboot.backend.core.model.CreateInfo;
import advanced.com.demo.springboot.backend.user.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @Id
    @SequenceGenerator(
            name="user_details_sequence",
            sequenceName = "user_details_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_details_sequence"
    )
    @Column(name = "user_details_id")
    private Long id;

    @NotBlank(message = "First Name is required")
    @NotNull
    private String firstName;
    private String middleName;
    @NotBlank(message = "Last Name is required")
    @NotNull
    private String lastName;
    @ReadOnlyProperty
    private String fullName;

    @NotBlank(message = "Date of Birth is required")
    @NotNull
    private LocalDate dateOfBirth;
    @ReadOnlyProperty
    private Number age;
    private  String address;

    @Column(nullable = true)
    private String photo;

    @JsonIgnoreProperties({"createInfo"})
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(
            name="user_id", referencedColumnName = "user_id"
    )
    private User user;

    @Embedded
    private CreateInfo createInfo;
    @PrePersist
    protected void onCreate() {
        final var data = new CreateInfo();
        data.setCreatedAtAd(LocalDateTime.now());
        this.createInfo = data;
        this.fullName = this.firstName +  "" + this.lastName;
        this.age = LocalDate.now().getYear() - this.getDateOfBirth().getYear();
    }
    @PreUpdate
    protected void onUpdate(){
        createInfo.setUpdatedAtAd(LocalDateTime.now());
    }

}
