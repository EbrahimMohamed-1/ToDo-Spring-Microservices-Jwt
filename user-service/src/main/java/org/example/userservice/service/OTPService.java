package org.example.userservice.service;

import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.OTP;
import org.example.userservice.entity.User;
import org.example.userservice.exception.exceptiontype.OtpInvalidException;
import org.example.userservice.repository.OTPRepository;
import org.example.userservice.security.userdetailsserviceimpl.UserDetailsServiceImpl;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OTPService{

    private final OTPRepository otpRepository;
    private final EmailService emailService;
    private final UserDetailsServiceImpl userDetailsService;

    public void generateAndSendOtp(String email) {
        String otpCode = String.valueOf(new Random().nextInt(9000) + 1000);
        User user = userDetailsService.loadUserByUsername(email);
        OTP otp = OTP.builder()
                .otp(otpCode)
                .expirationTime(LocalDateTime.now().plusMinutes(5))
                .user(user)
                .build();

        otpRepository.save(otp);
        emailService.sendOtpEmail(user.getEmail(), otpCode);
    }


    public void deleteAllOtpCodes(String email) {
        User user = userDetailsService.loadUserByUsername(email);

        otpRepository.deleteByUser(user);

    }


    public void isValidOtp(String email, String inputOtp) {

        User user = userDetailsService.loadUserByUsername(email);

       OTP otp = otpRepository.findByUser(user);


        if (otp.getExpirationTime().isBefore(LocalDateTime.now())) {
            throw new OtpInvalidException("OTP has expired. Please request a new one.");
        }

        if (!otp.getOtp().equals(inputOtp)) {
            throw new OtpInvalidException("Invalid OTP. Please try again.");
        }
    }
}
