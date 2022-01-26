package com.erensayar.hrDayOffModuleApplication.model.entity;

import com.erensayar.hrDayOffModuleApplication.model.enums.LeaveTypeEnum;
import com.erensayar.hrDayOffModuleApplication.model.enums.RequestStatusEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@ApiModel(value = "Request Of Leave Model")
public class RequestOfLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{hrDayOfModule.constraint.NotNull.message}")
    private String reason;

    @NotNull(message = "{hrDayOfModule.constraint.NotNull.message}")
    private LocalDate startDateOfLeave;

    @NotNull(message = "{hrDayOfModule.constraint.NotNull.message}")
    private LocalDate endDateOfLeave;

    private Double wantedTotalTime; // endDateOfLeave-startDateOfLeave

    private String requestOpeningTime;

    @NotNull(message = "{hrDayOfModule.constraint.NotNull.message}")
    @Enumerated(EnumType.ORDINAL)
    private LeaveTypeEnum leaveType;

    @Enumerated(EnumType.ORDINAL)
    private RequestStatusEnum status;

    // This constructor for create
    public RequestOfLeave(String reason,
                          LocalDate startDateOfLeave,
                          LocalDate endDateOfLeave,
                          LeaveTypeEnum leaveType) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.reason = reason;
        this.startDateOfLeave = startDateOfLeave;
        this.endDateOfLeave = endDateOfLeave;
        this.leaveType = leaveType;
        this.wantedTotalTime = calculateWantedTotalTime(startDateOfLeave, endDateOfLeave);
        this.requestOpeningTime = LocalDateTime.now().format(formatter);
        this.status = RequestStatusEnum.AWAITING_APPROVAL;
    }

    // This constructor for update
    public RequestOfLeave(Long id,
                          String reason,
                          LocalDate startDateOfLeave,
                          LocalDate endDateOfLeave,
                          String requestOpeningTime,
                          LeaveTypeEnum leaveType,
                          RequestStatusEnum status) {
        this.id = id;
        this.reason = reason;
        this.startDateOfLeave = startDateOfLeave;
        this.endDateOfLeave = endDateOfLeave;
        this.wantedTotalTime = calculateWantedTotalTime(startDateOfLeave, endDateOfLeave);
        this.requestOpeningTime = requestOpeningTime;
        this.leaveType = leaveType;
        this.status = status;
    }

    // 1. ister -> Tested. 100% worked for every situation.
    private Double calculateWantedTotalTime(LocalDate startDateOfLeave, LocalDate endDateOfLeave) {
        long counterOfPublicHolidays = 0;
        long dayToBeAdded = 0;
        long grossCalculate = ChronoUnit.DAYS.between(startDateOfLeave, endDateOfLeave);
        // First day is a holiday control
        int dayOfWeekForFirstDay = startDateOfLeave.getDayOfWeek().getValue();
        if (dayOfWeekForFirstDay == 6 || dayOfWeekForFirstDay == 7) {
            counterOfPublicHolidays++;
        }
        // Then other days control, are they holiday?
        for (int i = startDateOfLeave.getDayOfYear(); i <= endDateOfLeave.getDayOfYear(); i++) {
            dayToBeAdded++;
            LocalDate nextDay = startDateOfLeave.plusDays(dayToBeAdded);
            int dayOfWeek = nextDay.getDayOfWeek().getValue();
            if (dayOfWeek == 6 || dayOfWeek == 7) {
                counterOfPublicHolidays++;
            }
        }
        // Then subtraction holidays
        return (double) grossCalculate - counterOfPublicHolidays;
    }
}
