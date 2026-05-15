package com.medical.appointment.service;

import com.medical.appointment.model.LabOrderItem;
import com.medical.appointment.model.enums.AccessLevel;
import com.medical.appointment.repository.LabOrderItemRepository;
import com.medical.appointment.security.SecurityAccessUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LabOrderItemService {

    private final LabOrderItemRepository labOrderItemRepository;
    private final SecurityAccessUtil securityAccessUtil;

    @Transactional
    public void updateItemStatus(Integer itemId, String status) {
        securityAccessUtil.validateAdminLevel(AccessLevel.FULL, AccessLevel.SUPER_ADMIN);

        LabOrderItem item = labOrderItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));

        item.setStatus(status.toUpperCase());
        labOrderItemRepository.save(item);
    }
}
