package com.medical.appointment.repository;

import com.medical.appointment.model.BillingItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingItemRepository extends JpaRepository<BillingItem, Integer> {


    List<BillingItem> findByBilling_BillingId(Integer billingId);

}