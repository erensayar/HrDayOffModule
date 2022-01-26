package com.erensayar.hrDayOffModuleApplication.model.dto;

import com.erensayar.hrDayOffModuleApplication.model.enums.LeaveTypeEnum;
import com.erensayar.hrDayOffModuleApplication.model.enums.RequestStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Received object if save or update to db as directly, this situation creates a security vulnerability.
 * This class created for made to avoid security vulnerabilities.
 */
@Getter
@Setter
@Builder
public class RequestOfLeaveDto {

    private Long id;
    private String reason;
    private LocalDate startDateOfLeave;
    private LocalDate endDateOfLeave;
    private LeaveTypeEnum leaveType;

    // Down lines set in constructor
    private String requestOpeningTime;
    private RequestStatusEnum status;
    private Long wantedTotalTime;
}
