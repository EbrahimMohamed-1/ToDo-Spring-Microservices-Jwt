package org.example.userservice.repository;

import org.example.userservice.entity.OTP;
import org.example.userservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OTPRepository extends JpaRepository<OTP,Integer> {
    OTP findByUserId(User userId);
    void deleteByUserId(User userId);

}
