package com.medical.appointment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "laboratories")
@Getter
@Setter
@NoArgsConstructor
public class Laboratory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "laboratory_id")
    private Integer laboratoryId;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false)
    private String name; [cite: 1]

    @NotNull
    @Column(nullable = false)
    private String address; [cite: 1]

    @NotNull
    @Column(name = "opening_hours", nullable = false)
    private String openingHours; [cite: 1]

    @NotNull
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String phone; [cite: 1]

    @NotNull
    @Email
    @Column(nullable = false)
    private String email; [cite: 1]

    @NotNull
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; [cite: 1]

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; [cite: 1]

    @OneToMany(mappedBy = "laboratory", fetch = FetchType.LAZY)
    private List<LabOrder> labOrders; [cite: 1]

    @OneToMany(mappedBy = "laboratory", fetch = FetchType.LAZY)
    private List<LabTest> labTests; [cite: 1]

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}