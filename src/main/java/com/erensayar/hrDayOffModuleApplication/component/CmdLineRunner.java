package com.erensayar.hrDayOffModuleApplication.component;

import com.erensayar.hrDayOffModuleApplication.model.dto.EmployeeDto;
import com.erensayar.hrDayOffModuleApplication.model.dto.RequestOfLeaveDto;
import com.erensayar.hrDayOffModuleApplication.model.entity.RequestOfLeave;
import com.erensayar.hrDayOffModuleApplication.model.enums.LeaveTypeEnum;
import com.erensayar.hrDayOffModuleApplication.service.EmployeeService;
import com.erensayar.hrDayOffModuleApplication.service.RequestOfLeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class help to create test data
 */
@RequiredArgsConstructor
@Component
public class CmdLineRunner implements CommandLineRunner {

    private final EmployeeService employeeService;
    private final RequestOfLeaveService requestOfLeaveService;

    @Override
    public void run(String... args) throws Exception {

        RequestOfLeaveDto request1 = RequestOfLeaveDto.builder()
                .reason("Doktora gitmem gerekiyor")
                .startDateOfLeave(LocalDate.of(2022, 1, 15))
                .endDateOfLeave(LocalDate.of(2022, 1, 19))
                .leaveType(LeaveTypeEnum.EXCUSE_LEAVE)
                .build();

        RequestOfLeaveDto request2 = RequestOfLeaveDto.builder()
                .reason("Covid-19 oldum, raporluyum")
                .startDateOfLeave(LocalDate.of(2022, 1, 26))
                .endDateOfLeave(LocalDate.of(2022, 2, 15))
                .leaveType(LeaveTypeEnum.SICKNESS_LEAVE)
                .build();

        RequestOfLeaveDto request3 = RequestOfLeaveDto.builder()
                .reason("Doğum yapacağım.")
                .startDateOfLeave(LocalDate.of(2022, 1, 1))
                .endDateOfLeave(LocalDate.of(2022, 1, 28))
                .leaveType(LeaveTypeEnum.CHILDBEARING_LEAVE)
                .build();

        RequestOfLeaveDto request4 = RequestOfLeaveDto.builder()
                .reason("Çocuğu okuldan almam gerekiyor.")
                .startDateOfLeave(LocalDate.of(2022, 1, 1))
                .endDateOfLeave(LocalDate.of(2022, 1, 15))
                .leaveType(LeaveTypeEnum.CHILDBEARING_LEAVE)
                .build();

        RequestOfLeaveDto request5 = RequestOfLeaveDto.builder()
                .reason("Create Metodu İçin Deneme")
                .startDateOfLeave(LocalDate.of(2022, 1, 4))
                .endDateOfLeave(LocalDate.of(2022, 3, 6))
                .leaveType(LeaveTypeEnum.ANNUAL_PAID_LEAVE)
                .build();

        RequestOfLeaveDto request6 = RequestOfLeaveDto.builder()
                .reason("Create Metodu İçin Deneme")
                .startDateOfLeave(LocalDate.of(2022, 1, 26))
                .endDateOfLeave(LocalDate.of(2022, 1, 28))
                .leaveType(LeaveTypeEnum.NATIONAL_HOLIDAYS_GENERAL_HOLIDAYS_LEAVE)
                .build();

        RequestOfLeave requestOfLeave1 = requestOfLeaveService.createRequestOfLeave(request1);
        RequestOfLeave requestOfLeave2 = requestOfLeaveService.createRequestOfLeave(request2);
        RequestOfLeave requestOfLeave3 = requestOfLeaveService.createRequestOfLeave(request3);
        RequestOfLeave requestOfLeave4 = requestOfLeaveService.createRequestOfLeave(request4);
        RequestOfLeave requestOfLeave5 = requestOfLeaveService.createRequestOfLeave(request5); // Created for postman test
        RequestOfLeave requestOfLeave6 = requestOfLeaveService.createRequestOfLeave(request6); // Created for postman test

        List<Long> requestOfLeaveList1 = new ArrayList<>();
        requestOfLeaveList1.add(requestOfLeave1.getId());
        requestOfLeaveList1.add(requestOfLeave2.getId());

        List<Long> requestOfLeaveList2 = new ArrayList<>();
        requestOfLeaveList2.add(requestOfLeave3.getId());

        List<Long> requestOfLeaveList3 = new ArrayList<>();
        requestOfLeaveList3.add(requestOfLeave4.getId());

        LocalDate startDateOfWork1 = LocalDate.of(2021, 2, 1);
        LocalDate startDateOfWork2 = LocalDate.of(2015, 3, 2);
        LocalDate startDateOfWork3 = LocalDate.of(2019, 10, 30);

        EmployeeDto employee1 = EmployeeDto.builder()
                .name("Eren")
                .surname("Sayar")
                .phoneNumber("05332330001")
                .companyMail("eren.sayar1@company.com")
                .personalMail("eren.sayar1@yandex.com")
                .unusedDayOff(5.0)
                .startDateOfWork(startDateOfWork1)
                .requestOfLeaveIdNumbers(requestOfLeaveList1)
                .build();

        EmployeeDto employee2 = EmployeeDto.builder()
                .name("Eren")
                .surname("Sayar")
                .phoneNumber("05332330002")
                .companyMail("eren.sayar2@company.com")
                .personalMail("eren.sayar2@yandex.com")
                .unusedDayOff(24.0)
                .startDateOfWork(startDateOfWork2)
                .requestOfLeaveIdNumbers(requestOfLeaveList2)
                .build();

        EmployeeDto employee3 = EmployeeDto.builder()
                .name("Egor")
                .surname("Mikhailovic")
                .phoneNumber("05332330003")
                .companyMail("egor.mikhailovic@company.com")
                .personalMail("egormikhailovic@yandex.com")
                .unusedDayOff(12.0)
                .startDateOfWork(startDateOfWork3)
                .requestOfLeaveIdNumbers(requestOfLeaveList3)
                .build();

        employeeService.createEmployee(employee1);
        employeeService.createEmployee(employee2);
        employeeService.createEmployee(employee3);


    }
}
