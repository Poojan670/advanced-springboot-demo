package advanced.com.demo.springboot.backend.core.model;

import lombok.Data;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.Embeddable;
import java.time.LocalDateTime;


@Embeddable
@Data
public class CreateInfo {

    @ReadOnlyProperty
    private LocalDateTime createdAtAd;
    @ReadOnlyProperty
    private LocalDateTime updatedAtAd;
}
