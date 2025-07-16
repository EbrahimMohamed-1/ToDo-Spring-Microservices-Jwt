package com.example.todoservice.entity;

import com.example.todoservice.enums.Priority;
import com.example.todoservice.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class TaskDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String description;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    private Priority priority;
    @Enumerated(EnumType.STRING)
    private Status status;

    @Override
    public String toString() {
        return "TaskDetails{" +
                "Id=" + Id +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", priority=" + priority +
                ", status=" + status +
                '}';
    }
}