package com.shopbasket.userservice.Repository;

import com.shopbasket.userservice.Entities.ConfirmationEmailToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationEmailTokenRepository extends JpaRepository<ConfirmationEmailToken,Long> {
    Optional<ConfirmationEmailToken> findByToken(String token);

    Optional<ConfirmationEmailToken> deleteAllByEmail(String  email);

    @Transactional
    @Modifying
    @Query("UPDATE ConfirmationEmailToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token, LocalDateTime confirmedAt);

    Optional<ConfirmationEmailToken> findByEmail(String email);
}