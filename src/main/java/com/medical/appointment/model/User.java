package com.medical.appointment.model;

import com.medical.appointment.model.enums.Gender;
import com.medical.appointment.model.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true, length = 100)
    @NotNull(message = "Email cannot be null")
    private String email;

    @Column(nullable = false)
    @NotNull(message = "Password cannot be null")
    private String password;

    @Column(nullable = false)
    private int roleType = UserRole.PATIENT.getValue();

    @Column(nullable = false, length = 50)
    @NotNull(message = "First name cannot be null")
    private String firstName;

    @Column(nullable = false, length = 50)
    @NotNull(message = "Last name cannot be null")
    private String lastName;

    @Column(nullable = false, length = 15)
    @NotNull(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number format")
    private String phone;

    @Column(nullable = false, unique = true)
    @NotNull(message = "NIC cannot be null")
    @Pattern(regexp = "^([0-9]{9}[Vv]|[0-9]{12})$", message = "Invalid NIC format")
    private String NIC;

    @Column(nullable = false)
    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @Column(nullable = false, length = 255)
    @NotNull(message = "Address cannot be null")
    private String address;

    @Column(nullable = false)
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void validateRole() {
        if (this.roleType == 0) {
            this.roleType = UserRole.PATIENT.getValue();
        }
    }

    public void setRole(UserRole role) {
        if (role != null) {
            this.roleType = role.getValue();
        }
    }
}