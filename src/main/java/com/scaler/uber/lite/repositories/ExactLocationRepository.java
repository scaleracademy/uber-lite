package com.scaler.uber.lite.repositories;

import com.scaler.uber.lite.models.ExactLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExactLocationRepository extends JpaRepository<ExactLocation, Long> {
}
