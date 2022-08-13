package advanced.com.demo.springboot.backend.helper.token;


import advanced.com.demo.springboot.backend.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ConfirmToken {

    @Id
    @SequenceGenerator(
            name="token_sequence",
            sequenceName="token_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_sequence"
    )

    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private LocalDateTime expires_at;
    private LocalDateTime confirmed_at;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name="users"
    )
    private User appUser;


    public ConfirmToken(String token,
                        LocalDateTime created_at,
                        LocalDateTime expires_at,
                        User appUser
    ) {
        this.token = token;
        this.created_at = created_at;
        this.expires_at = expires_at;
        this.appUser= appUser;
    }
}

