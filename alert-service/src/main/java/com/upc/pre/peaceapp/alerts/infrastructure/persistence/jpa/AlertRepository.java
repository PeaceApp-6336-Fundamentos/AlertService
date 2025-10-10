package com.upc.pre.peaceapp.alerts.infrastructure.persistence.jpa;

import com.upc.pre.peaceapp.alerts.domain.model.aggregates.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findAllByUserId(Long userId);
    Alert findById(long alertId);
    void deleteAllByUserId(Long userId);
}
