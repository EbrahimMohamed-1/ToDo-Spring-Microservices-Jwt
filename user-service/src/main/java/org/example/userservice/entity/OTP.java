package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Validated
@Builder
@Entity
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String otp;

    @CreationTimestamp
    @Column(name = "expiration_time")
    private LocalDateTime expirationTime;


    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    public boolean isExpired(){
        return expirationTime.isBefore(LocalDateTime.now());
    }
}
