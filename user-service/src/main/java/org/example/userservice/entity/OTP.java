package org.example.userservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@Validated
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
    @JoinColumn(name = "user_id")
    private User userId;

    public boolean isExpired(){
        return expirationTime.isBefore(LocalDateTime.now());
    }
}
