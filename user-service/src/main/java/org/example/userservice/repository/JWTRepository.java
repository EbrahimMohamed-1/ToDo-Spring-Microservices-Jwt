package org.example.userservice.repository;

import org.example.userservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JWTRepository extends JpaRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken r SET r.revoked = true WHERE r.user.email = :email AND r.revoked = false")
    void revokeAllTokensByUser(String email);
    @Modifying
    @Query("DELETE FROM RefreshToken as r where r.user.email = :email")
    void deleteAllByUserEmail(String email);

}
