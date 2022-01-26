package com.erensayar.hrDayOffModuleApplication.repo;

import com.erensayar.hrDayOffModuleApplication.model.entity.RequestOfLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestOfLeaveRepo extends JpaRepository<RequestOfLeave, Long> {
}
