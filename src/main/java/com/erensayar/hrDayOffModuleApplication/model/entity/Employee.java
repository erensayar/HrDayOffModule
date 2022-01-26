package com.erensayar.hrDayOffModuleApplication.model.entity;

import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@ApiModel(value = "Employee Model")
public class Employee {

    @Id
    private String id;

    @NotNull(message = "{hrDayOffModule.constraint.NotNull.message}")
    private String name;

    @NotNull(message = "{hrDayOffModule.constraint.NotNull.message}")
    private String surname;

    @NotNull(message = "{hrDayOffModule.constraint.NotNull.message}")
    @Size(min = 11, max = 11, message = "{hrDayOffModule.constraint.phoneNumber.Size.message}")
    @Column(unique = true, length = 11)
    private String phoneNumber;

    @NotNull(message = "{hrDayOffModule.constraint.NotNull.message}")
    @Pattern(regexp = "^(.+)@(.+)$", message = "{hrDayOffModule.constraint.mail.Pattern.message}")
    @Column(unique = true, length = 50)
    private String companyMail;

    @NotNull(message = "{hrDayOffModule.constraint.NotNull.message}")
    @Pattern(regexp = "^(.+)@(.+)$", message = "{hrDayOffModule.constraint.mail.Pattern.message}")
    @Column(unique = true, length = 50)
    private String personalMail;

    // will be calculated using the startDateOfLeave and endDateOfLeave values. (startDateOfLeave,endDateOfLeave -> RequestOfLeave properties)
    // Also initial value is 5 because new employee has 5 days up to 1 year.
    // If 1 year expires, adjustments will be made in setStatusOfRequest method.
    private Double unusedDayOff;

    @NotNull(message = "{hrDayOffModule.constraint.NotNull.message}")
    private LocalDate startDateOfWork;

    @OneToMany
    private List<RequestOfLeave> requests;

}
