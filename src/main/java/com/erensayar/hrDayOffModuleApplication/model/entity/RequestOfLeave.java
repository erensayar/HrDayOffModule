package com.erensayar.hrDayOffModuleApplication.model.entity;

import com.erensayar.hrDayOffModuleApplication.model.enums.LeaveTypeEnum;
import com.erensayar.hrDayOffModuleApplication.model.enums.RequestStatusEnum;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
}
