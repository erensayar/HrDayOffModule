package com.erensayar.hrDayOffModuleApplication.component;

import com.erensayar.hrDayOffModuleApplication.model.dto.EmployeeDto;
import com.erensayar.hrDayOffModuleApplication.model.entity.RequestOfLeave;
import com.erensayar.hrDayOffModuleApplication.model.enums.LeaveTypeEnum;
import com.erensayar.hrDayOffModuleApplication.repo.RequestOfLeaveRepo;
import com.erensayar.hrDayOffModuleApplication.service.EmployeeService;
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
    private final RequestOfLeaveRepo requestOfLeaveRepo;

    @Override
    public void run(String... args) throws Exception {

        RequestOfLeave request1 = new RequestOfLeave(
                "Doktora gitmem gerekiyor",
                LocalDate.of(2022, 1, 15),
                LocalDate.of(2022, 1, 19),
                LeaveTypeEnum.EXCUSE_LEAVE);

        RequestOfLeave request2 = new RequestOfLeave(
                "Covid-19 oldum, raporluyum",
                LocalDate.of(2022, 1, 26),
                LocalDate.of(2022, 2, 15),
                LeaveTypeEnum.SICKNESS_LEAVE);

        RequestOfLeave request3 = new RequestOfLeave(
                "Doğum yapacağım.",
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 28),
                LeaveTypeEnum.CHILDBEARING_LEAVE);

        RequestOfLeave request4 = new RequestOfLeave(
                "Çocuğu okuldan almam gerekiyor.",
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 5),
                LeaveTypeEnum.EXCUSE_LEAVE);

        RequestOfLeave request5 = new RequestOfLeave(
                "Create Metodu İçin Deneme",
                LocalDate.of(2022, 1, 4),
                LocalDate.of(2022, 3, 6),
                LeaveTypeEnum.ANNUAL_PAID_LEAVE);

        RequestOfLeave request6 = new RequestOfLeave(
                "Create Metodu İçin Deneme",
                LocalDate.of(2022, 1, 26),
                LocalDate.of(2022, 1, 28),
                LeaveTypeEnum.NATIONAL_HOLIDAYS_GENERAL_HOLIDAYS_LEAVE);

        RequestOfLeave requestOfLeave1 = requestOfLeaveRepo.save(request1);
        RequestOfLeave requestOfLeave2 = requestOfLeaveRepo.save(request2);
        RequestOfLeave requestOfLeave3 = requestOfLeaveRepo.save(request3);
        RequestOfLeave requestOfLeave4 = requestOfLeaveRepo.save(request4);
        RequestOfLeave requestOfLeave5 = requestOfLeaveRepo.save(request5); // Created for postman test
        RequestOfLeave requestOfLeave6 = requestOfLeaveRepo.save(request6); // Created for postman test

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
