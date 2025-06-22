package org.example.userservice.repository;

import org.example.userservice.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OTPRepository extends JpaRepository<OTP,Integer> {

}
