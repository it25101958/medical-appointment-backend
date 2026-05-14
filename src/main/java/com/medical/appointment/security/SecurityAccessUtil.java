package com.medical.appointment.security;

import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.model.enums.StaffStatus;
import com.medical.appointment.repository.AdminRepository;
import com.medical.appointment.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component("securityUtil")
@RequiredArgsConstructor
public class SecurityAccessUtil {

    private final AdminRepository adminRepository;
    private final StaffRepository staffRepository;

    public void validateModificationAccess(String targetUserEmail) {
        if (hasAnyRole("ROLE_READ_ONLY")) {
            throw new AccessDeniedException("Access Denied: Read-only accounts cannot modify data.");
        }

        boolean canModifyOthers = hasAnyRole("ROLE_SUPER_ADMIN", "ROLE_FULL");
        boolean isOwner = isOwner(targetUserEmail);

        if (!(canModifyOthers || isOwner)) {
            throw new AccessDeniedException("Access Denied: You do not have permission to modify this record.");
        }
    }

    public void validateAdminLevel(AccessLevel... allowedLevels) {
        String email = getCurrentUserEmail();
        adminRepository.findByUserEmail(email)
                .ifPresentOrElse(admin -> {
                    if (!Arrays.asList(allowedLevels).contains(admin.getAccessLevel())) {
                        throw new AccessDeniedException("Access Denied: Required level " + Arrays.toString(allowedLevels));
                    }
                }, () -> {
                    throw new AccessDeniedException("Access Denied: Admin record not found.");
                });
    }

    public void validateStaffStatus(StaffStatus... allowedStatuses) {
        String email = getCurrentUserEmail();
        staffRepository.findByUserEmail(email)
                .ifPresentOrElse(staff -> {
                    if (!Arrays.asList(allowedStatuses).contains(staff.getStatus())) {
                        throw new AccessDeniedException("Access Denied: Your status is " + staff.getStatus().getLabel());
                    }
                }, () -> { throw new AccessDeniedException("Access Denied: Staff record not found."); });
    }

    public boolean isOwner(String targetUserEmail) {
        String currentEmail = getCurrentUserEmail();
        return currentEmail != null && currentEmail.equalsIgnoreCase(targetUserEmail);
    }

    public String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : null;
    }

    public boolean hasAnyRole(String... roles) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;

        return auth.getAuthorities().stream()
                .anyMatch(a -> Arrays.asList(roles).contains(a.getAuthority()));
    }
}