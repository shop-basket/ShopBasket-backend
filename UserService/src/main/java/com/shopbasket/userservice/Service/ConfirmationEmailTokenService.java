package com.shopbasket.userservice.Service;

import com.shopbasket.userservice.Entities.ConfirmationEmailToken;
import com.shopbasket.userservice.Repository.ConfirmationEmailTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationEmailTokenService {
    private final ConfirmationEmailTokenRepository confirmationEmailTokenRepository;
    public void saveConfirmationToken(ConfirmationEmailToken confirmationEmailToken){
        confirmationEmailTokenRepository.save(confirmationEmailToken);
    }
    public Optional<ConfirmationEmailToken> getToken(String token) {
        return confirmationEmailTokenRepository.findByToken(token);
    }
    public int setConfirmedAt(String token) {
        return confirmationEmailTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
