package advanced.com.demo.springboot.backend.helper.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public void saveToken(ConfirmToken token) {
        tokenRepository.save(token);
    }

    public Optional<ConfirmToken> getToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public int setConfirmedAt(String Token) {
        return tokenRepository.updateConfirmedAt(
                Token, LocalDateTime.now()
        );
    }
}

