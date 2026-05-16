package com.medical.appointment.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LabTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Size(max = 100)
    @Column(unique = true, length = 100, nullable = false)
    private String testName;

    @NotNull
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private String category;

    @Size(max = 255)
    @Column(length = 255)
    private String description;

    @NotNull
    @Digits(integer = 8, fraction = 2)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal standardPrice;

    @NotNull
    @Column(columnDefinition = "boolean default true")
    private Boolean isActive = true;

    @OneToMany(mappedBy = "labTest", fetch = FetchType.LAZY)
    private List<LabOrderItem> labOrderItems;

    @ManyToOne
    @JoinColumn(name = "laboratory_id")
    private Laboratory laboratory;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}