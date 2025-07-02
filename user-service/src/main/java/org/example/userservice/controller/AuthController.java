package org.example.userservice.controller;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.*;
import org.example.userservice.entity.RefreshToken;
import org.example.userservice.exception.exceptiontype.MissingTokenException;
import org.example.userservice.repository.JWTRepository;
import org.example.userservice.service.AuthService;
import org.example.userservice.service.EmailService;
import org.example.userservice.service.JWTService;
import org.example.userservice.service.OTPService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final JWTRepository jWTRepository;
    private final EmailService emailService;
    private final AuthService authService;
    private final OTPService otpservice;
    private final JWTService jwtService;



    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {

        otpservice.generateAndSendOtp(request.getEmail());

        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        LoginResponse response = authService.login(request, httpRequest);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", response.getRefreshToken() )
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();


        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(

                        LoginResponse.builder().
                                email(response.getEmail())
                                .accessToken(response.getAccessToken())
                                .build()
                );


    }

    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam String email, @RequestParam String code) {
        otpservice.isValidOtp(email, code);
        authService.enableUser(email);
        return ResponseEntity.ok("Activated");

    }


    @PostMapping("regenerate-otp")
    public ResponseEntity<String> sendOtpEmail(@RequestParam String email) {
        try {
            if (email == null || email.isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            otpservice.deleteAllOtpCodes(email);
            otpservice.generateAndSendOtp(email);
            return ResponseEntity.ok("OTP email sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to send OTP: " + e.getMessage());
        }
    }

    @GetMapping("/checkToken")
    public ResponseEntity<UserResponseInfo> checkToken(HttpServletRequest request) {
        String token = jwtService.resolveToken(request);
        UserResponseInfo user = jwtService.checkToken(token);
        return ResponseEntity.ok(user);
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.resolveRefreshToken(request);
        jwtService.revokeRefreshToken(refreshToken);

        // Clear the refresh token cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/logout-all-devices")
    public ResponseEntity<String> logoutFromAllDevices(HttpServletRequest request, HttpServletResponse response) {
        String refreshTokenStr = jwtService.resolveRefreshToken(request);

        RefreshToken refreshToken = jWTRepository.findByToken(refreshTokenStr)
                .orElseThrow(() -> new JwtException("Refresh token not found"));
        String email = refreshToken.getUser().getEmail();
        jwtService.revokeAllRefreshTokens(email);
        emailService.sendSecurityAlert(email);

        // Clear the refresh token cookie
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(0)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok("Logged out successfully");
    }


    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = jwtService.resolveRefreshToken(request);

        String ip = request.getRemoteAddr();
        String userAgent = request.getHeader("User-Agent");

        Map<String, String> tokens = jwtService.refreshAccessToken(refreshToken, ip, userAgent);
        String accessToken = tokens.get("accessToken");
        String newRefreshToken = tokens.get("refreshToken");

        ResponseCookie cookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/api/v1/auth/refresh")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(accessToken);
    }


}
