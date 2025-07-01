package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.userservice.utils.TokenType;
import org.hibernate.annotations.CreationTimestamp;


import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column( nullable = false, unique = true)
    private String token;

    private String userAgent;
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "issued_at")
    private LocalDateTime issuedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "session_start_time")
    private LocalDateTime sessionStartTime;

    private boolean revoked;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

}
