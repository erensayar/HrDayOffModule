package com.erensayar.hrDayOffModuleApplication.service;

import com.erensayar.hrDayOffModuleApplication.model.dto.EmployeeDto;
import com.erensayar.hrDayOffModuleApplication.model.entity.Employee;

import java.util.List;

public interface EmployeeService {

    Employee createEmployee(EmployeeDto employeeDto);

    Employee getEmployeeById(String id);

    List<Employee> getEmployees();

    Employee updateEmployee(EmployeeDto employeeDto);

    void deleteEmployeeById(String id);

}
