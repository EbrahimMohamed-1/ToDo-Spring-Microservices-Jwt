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
public class JWT {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column( nullable = false, unique = true)
    private String token;

    private boolean revoked;

    @Column(name = "expired")
    private boolean expired;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

}
