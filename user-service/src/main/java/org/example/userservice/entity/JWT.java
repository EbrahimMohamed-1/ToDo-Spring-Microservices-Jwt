package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.userservice.utils.TokenType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class JWT {
    @Id
    private int id;

    @Column( nullable = false, unique = true)
    private String token;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

}
