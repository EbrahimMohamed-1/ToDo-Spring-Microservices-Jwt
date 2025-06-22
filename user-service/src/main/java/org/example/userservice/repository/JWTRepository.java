package org.example.userservice.repository;

import org.example.userservice.entity.JWT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JWTRepository extends JpaRepository<JWT,Integer> {
    JWT findByToken(String token);
    void deleteByToken(String token);

}
