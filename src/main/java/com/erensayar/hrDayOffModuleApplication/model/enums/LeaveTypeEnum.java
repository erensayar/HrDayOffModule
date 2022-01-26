package com.erensayar.hrDayOffModuleApplication.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum LeaveTypeEnum {

    EXCUSE_LEAVE(0),
    ANNUAL_PAID_LEAVE(1),
    SICKNESS_LEAVE(2),
    NATIONAL_HOLIDAYS_GENERAL_HOLIDAYS_LEAVE(3),
    JOB_SEARCH_LEAVE(4),
    BREASTFEEDING_LEAVE(5),
    CHILDBEARING_LEAVE(6),
    AFTER_BIRTH_UNPAID_LEAVE(7);

    private final Integer leaveTypeId;

}
