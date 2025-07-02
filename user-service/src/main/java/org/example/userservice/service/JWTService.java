package org.example.userservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dto.UserResponseInfo;
import org.example.userservice.entity.RefreshToken;
import org.example.userservice.entity.User;
import org.example.userservice.repository.JWTRepository;
import org.example.userservice.security.userdetailsserviceimpl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class JWTService {

    private final JWTRepository jWTRepository;
    @Value("${env_variables.JWT_SECRET}")
    private String JWT_SECRET;

    @Value("${env_variables.JWT_EXPIRATION_MS}")
    private Long JWT_EXPIRATION_MS;

    private final UserDetailsServiceImpl userDetailsService;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    public String createToken(User user) {

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshToken createRefreshTokenFirstTime(User user, String ip, String userAgent) {
        return createRefreshToken(user, ip, userAgent, LocalDateTime.now());
    }


    // create a refresh token
    public RefreshToken createRefreshToken(User user, String ip, String userAgent, LocalDateTime sessionStartTime) {
        String token = UUID.randomUUID().toString();

        RefreshToken refresh = RefreshToken.builder()
                .token(token)
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(7))
                .sessionStartTime(sessionStartTime)
                .user(user)
                .ipAddress(ip)
                .userAgent(userAgent)
                .revoked(false)
                .build();

        return refresh;
    }

    // extract token from the header
    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtException("Missing or malformed Authorization header");
        }
        return authHeader.substring(7); // remove "Bearer "
    }

    // extract refresh token from cookie
    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        throw new JwtException("Refresh token cookie not found");
    }

    // check token and return claims
    public Claims resolveClaims(HttpServletRequest req) {
        String token = resolveToken(req);
        return parseJwtClaims(token);
    }

    //extracting claims from token
    private Claims parseJwtClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenExpired(Date expirationDate) {
        return expirationDate.before(new Date(System.currentTimeMillis()));
    }

    // check if the token is valid
    public UserResponseInfo checkToken(String token) {
        Claims claims = parseJwtClaims(token);

        if (isTokenExpired(claims.getExpiration())) {
            throw new JwtException("Token has expired");
        }

        String email = claims.getSubject();
        User user = userDetailsService.loadUserByUsername(email);

        return new UserResponseInfo(user.getId(), user.getEmail(), user.isEnabled());
    }

    public void revokeRefreshToken(String refreshToken) {
        RefreshToken token = jWTRepository.findByToken(refreshToken).orElseThrow(() -> new JwtException("Refresh token not found"));
        token.setRevoked(true);
        jWTRepository.save(token);

    }

    public void revokeAllRefreshTokens(String email) {
        jWTRepository.revokeAllTokensByUser(email);
    }
    public void deleteAllRefreshTokens(String email) {
        jWTRepository.deleteAllByUserEmail(email);
    }

    // create a new access token using refresh token
    public Map<String, String> refreshAccessToken(String refreshTokenId, String ip, String userAgent) {
        RefreshToken storedRefreshToken = jWTRepository.findByToken(refreshTokenId)
                .orElseThrow(() -> new JwtException("Refresh token not found"));

        if (storedRefreshToken.isRevoked()) {
            deleteAllRefreshTokens( storedRefreshToken.getUser().getEmail());
            throw new JwtException("Refresh token has been revoked");
        }

        if (storedRefreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new JwtException("Refresh token has expired");
        }

        if (!storedRefreshToken.getIpAddress().equals(ip) || !storedRefreshToken.getUserAgent().equals(userAgent)) {

            throw new JwtException("Refresh token used from different device or IP");
        }
        LocalDateTime sessionStart = storedRefreshToken.getSessionStartTime();
        if (sessionStart.plusDays(30).isBefore(LocalDateTime.now())) {
            throw new JwtException("Session has expired. Please login again.");
        }

        // Invalidate old token
        storedRefreshToken.setRevoked(true);
        jWTRepository.save(storedRefreshToken);

        User user = storedRefreshToken.getUser();
        RefreshToken newRefreshToken = createRefreshToken(user, ip, userAgent, sessionStart);
        jWTRepository.save(newRefreshToken);

        return Map.of("accessToken", createToken(user), "refreshToken", newRefreshToken.getToken());
    }


}
