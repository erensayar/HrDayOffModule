package com.erensayar.hrDayOffModuleApplication.repo;

import com.erensayar.hrDayOffModuleApplication.model.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, String> {
}
