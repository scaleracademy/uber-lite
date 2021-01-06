package com.scaler.uber.lite.repositories;

import com.scaler.uber.lite.models.DBConstant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBConstantRepository extends JpaRepository<DBConstant, Long> {
}
