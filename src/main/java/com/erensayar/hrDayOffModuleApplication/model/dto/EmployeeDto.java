package com.erensayar.hrDayOffModuleApplication.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * Received object if save or update to db as directly, this situation creates a security vulnerability.
 * This class created for made to avoid security vulnerabilities.
 */
@Getter
@Setter
@Builder
public class EmployeeDto {
    private String id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String companyMail;
    private String personalMail;
    private Double unusedDayOff;
    private LocalDate startDateOfWork;
    private List<Long> requestOfLeaveIdNumbers; // client just send request's id numbers
}
