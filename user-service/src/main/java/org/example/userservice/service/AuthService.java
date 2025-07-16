package org.example.userservice.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.LoginRequest;
import org.example.userservice.dto.LoginResponse;
import org.example.userservice.dto.SignupRequest;
import org.example.userservice.dto.SignupResponse;
import org.example.userservice.entity.RefreshToken;
import org.example.userservice.entity.User;
import org.example.userservice.exception.exceptiontype.AccountNotActivatedException;
import org.example.userservice.exception.exceptiontype.EmailAlreadyFound;
import org.example.userservice.repository.JWTRepository;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.security.userdetailsserviceimpl.UserDetailsServiceImpl;
import org.example.userservice.utils.TokenType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final JWTService jwtService;
    private final JWTRepository jwtRepository;
    private final AuthenticationManager authenticationManager;

    public SignupResponse signup(SignupRequest request) {
        if (userDetailsService.loadUserByUsername(request.getEmail()) != null) {
           throw new EmailAlreadyFound("Try with another email, this one is already registered");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());
        String email = request.getEmail();
        User user = User.builder()
                .email(request.getEmail())
                .password(hashedPassword)
                .build();
        userRepository.save(user);
        return new SignupResponse(String.format("Successfully registered and OTP code has sent to %s", email));
    }


    public LoginResponse login(LoginRequest request,  HttpServletRequest httpRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String ip = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        User user = userDetailsService.loadUserByUsername(request.getEmail());

        if (!user.isEnabled()) {
            throw new AccountNotActivatedException("Account is not activated. Please verify your email with OTP.");
        }

        String accessToken = jwtService.createToken(user);
        RefreshToken refreshToken = jwtService.createRefreshTokenFirstTime(user, ip, userAgent);
        jwtRepository.save(refreshToken);

        return new LoginResponse(user.getEmail(), accessToken, refreshToken.getToken());

    }


    public void enableUser(String email) {
        User user = userDetailsService.loadUserByUsername(email);
        user.setEnabled(true);
        userRepository.save(user);
    }




//    private void saveUserRefreshToken(User user, String ip, String userAgent) {
//
//        jwtRepository.save( jwtService.createRefreshToken(user, ip, userAgent));
//    }


}
