package com.erensayar.hrDayOffModuleApplication.service.implementation;

import com.erensayar.hrDayOffModuleApplication.error.exception.BadRequestException;
import com.erensayar.hrDayOffModuleApplication.error.exception.NoContentException;
import com.erensayar.hrDayOffModuleApplication.error.exception.OkWithMessage;
import com.erensayar.hrDayOffModuleApplication.model.dto.RequestOfLeaveDto;
import com.erensayar.hrDayOffModuleApplication.model.entity.Employee;
import com.erensayar.hrDayOffModuleApplication.model.entity.RequestOfLeave;
import com.erensayar.hrDayOffModuleApplication.model.enums.RequestStatusEnum;
import com.erensayar.hrDayOffModuleApplication.repo.RequestOfLeaveRepo;
import com.erensayar.hrDayOffModuleApplication.service.EmployeeService;
import com.erensayar.hrDayOffModuleApplication.service.RequestOfLeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RequestOfLeaveServiceImpl implements RequestOfLeaveService {

    // INJECTIONS
    //<================================================================================================================>

    // Constructor Injection Fields
    private final RequestOfLeaveRepo requestOfLeaveRepo;

    // Setter Injection Fields
    private EmployeeService employeeService;

    @Autowired // Setter injection used because EmployeeService and RequestOfLeaveService are nested.
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // CONSTANTS
    //<================================================================================================================>

    @Value("${company.right-of-leave.twoToFiveYearsLeaveRightAsDay}")
    private Double twoToFiveYearsLeaveRightAsDay;

    @Value("${company.right-of-leave.fiveToTenYearsLeaveRightAsDay}")
    private Double fiveToTenYearsLeaveRightAsDay;

    @Value("${company.right-of-leave.moreThen10YearsLeaveRightAsDay}")
    private Double moreThen10YearsLeaveRightAsDay;

    // PUBLIC METHODS
    //<================================================================================================================>

    @Override
    public RequestOfLeave createRequestOfLeave(RequestOfLeaveDto requestOfLeaveDto) {
        RequestOfLeave requestOfLeave = converterOfRequestOfLeave(requestOfLeaveDto);
        return requestOfLeaveRepo.save(setConstantsOfRequestObject(requestOfLeave));
    }


    @Override
    public RequestOfLeave getRequestOfLeaveById(Long id) {
        return requestOfLeaveRepo.findById(id).orElseThrow(NoContentException::new);
    }

    @Override
    public List<RequestOfLeave> getRequestOfLeaveList() {
        return requestOfLeaveRepo.findAll();
    }

    @Override
    public RequestOfLeave updateRequestOfLeave(RequestOfLeaveDto requestOfLeaveDto) {
        if (requestOfLeaveDto.getId() == null) {
            throw new BadRequestException();
        }
        this.getRequestOfLeaveById(requestOfLeaveDto.getId()); // if request doesn't exist then we throw an error from getRequestOfLeaveById method. if exist then can execute down line
        RequestOfLeave updatedRequestOfLeave = converterOfRequestOfLeave(requestOfLeaveDto);
        return requestOfLeaveRepo.save(updatedRequestOfLeave);
    }

    @Override
    public void deleteRequestOfLeaveById(Long requestId, String employeeId) {
        // First of all delete relation and then we can delete request
        Employee employee = employeeService.getEmployeeById(employeeId);
        List<RequestOfLeave> requests = employee.getRequests();
        // Delete from employee's requests list
        requests.removeIf(requestOfLeave -> requestOfLeave.getId().equals(requestId));
        // Then we cam delete request from db
        requestOfLeaveRepo.deleteById(requestId);
    }

    @Override
    public RequestOfLeave setStatusOfRequest(String employeeId, Long requestId) {
        Employee employee = employeeService.getEmployeeById(employeeId);
        RequestOfLeave requestOfLeave = this.getRequestOfLeaveById(requestId);

        // (2. ister employeeservice'de sağlandı burada da 1 yıl geçince 5 gün avans sıfırlanıyor ve tablodaki atamalar yapıldı)
        LocalDate startDateOfWork = employee.getStartDateOfWork();
        LocalDate today = LocalDate.now();
        long totalWorkingTime = ChronoUnit.DAYS.between(startDateOfWork, today);
        if (totalWorkingTime >= 3652.42199) { // 10 + years
            employee.setUnusedDayOff(moreThen10YearsLeaveRightAsDay);
        }
        if (totalWorkingTime >= 1826.21099) { // 5 - 10
            employee.setUnusedDayOff(fiveToTenYearsLeaveRightAsDay);
        }
        if (totalWorkingTime >= 365.242199) { // 1 - 5
            employee.setUnusedDayOff(twoToFiveYearsLeaveRightAsDay);
        }

        Double requested = requestOfLeave.getWantedTotalTime();
        Double exist = employee.getUnusedDayOff();
        // Talep, var olan izin hakkından fazla mı kontrol edilir
        if (requested <= exist) {
            requestOfLeave.setStatus(RequestStatusEnum.APPROVED);
            employee.setUnusedDayOff(exist - requested);
            return requestOfLeaveRepo.save(requestOfLeave);
        } else {
            throw new OkWithMessage("Yeterli izin hakkı yok."); // TODO: property dosyasina yazılmalı
        }
    }

    // PRIVATE METHODS
    //<================================================================================================================>

    // Set Request Of Leave Object Constants
    private RequestOfLeave setConstantsOfRequestObject(RequestOfLeave request) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        request.setWantedTotalTime(calculateWantedTotalTime(request.getStartDateOfLeave(), request.getEndDateOfLeave()));
        request.setRequestOpeningTime(LocalDateTime.now().format(formatter));
        request.setStatus(RequestStatusEnum.AWAITING_APPROVAL);
        return request;
    }

    private RequestOfLeave converterOfRequestOfLeave(RequestOfLeaveDto requestOfLeaveDto) {
        return RequestOfLeave.builder()
                .id(requestOfLeaveDto.getId())
                .reason(requestOfLeaveDto.getReason())
                .startDateOfLeave(requestOfLeaveDto.getStartDateOfLeave())
                .endDateOfLeave(requestOfLeaveDto.getEndDateOfLeave())
                .requestOpeningTime(requestOfLeaveDto.getRequestOpeningTime())
                .leaveType(requestOfLeaveDto.getLeaveType())
                .status(requestOfLeaveDto.getStatus())
                .build();
    }

    // Tested. 100% worked for every situation.
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
