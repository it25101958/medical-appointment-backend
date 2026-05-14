package com.medical.appointment.model;

import com.medical.appointment.model.enums.MedicationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "medication")
@Getter
@Setter
@NoArgsConstructor
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer medicationId;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    @Column(name = "generic_name", nullable = false, length = 255)
    private String genericName;

    @NotNull
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String manufacturer;

    @NotNull
    @Column(nullable = false)
    private String dosage;

    @NotNull
    @Column(name = "dosage_form", nullable = false)
    private String dosageForm;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "status", nullable = false) // Renamed column to just 'status'
    private MedicationStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "medication", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<PrescriptionItem> prescriptionItems;
}