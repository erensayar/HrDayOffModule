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
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RequestOfLeaveServiceImpl implements RequestOfLeaveService {

    private final RequestOfLeaveRepo requestOfLeaveRepo;
    private EmployeeService employeeService;

    @Autowired // Setter injection used because EmployeeService and RequestOfLeaveService are nested.
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Value("${company.right-of-leave.twoToFiveYearsLeaveRightAsDay}")
    private Double twoToFiveYearsLeaveRightAsDay;

    @Value("${company.right-of-leave.fiveToTenYearsLeaveRightAsDay}")
    private Double fiveToTenYearsLeaveRightAsDay;

    @Value("${company.right-of-leave.moreThen10YearsLeaveRightAsDay}")
    private Double moreThen10YearsLeaveRightAsDay;

    @Override
    public RequestOfLeave createRequestOfLeave(RequestOfLeaveDto requestOfLeaveDto) {
        return requestOfLeaveRepo.save(converterOfRequestOfLeave(requestOfLeaveDto, "CREATE"));
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
        RequestOfLeave updatedRequestOfLeave = converterOfRequestOfLeave(requestOfLeaveDto, "UPDATE");
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
        /* 1 yıl doldurmamış kişiler için 5 gün izin atanmıştı bu metod tetiklendiğinde 1 yıl dolmuşsa0 ve 5 arasındaysa
        izin hakkı sıfırlanacak. Böylece Avans izin hakkı 1 yıldan sonra kullanılamayacak,bu avans geçerliliğini yitirecek. */
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

    private RequestOfLeave converterOfRequestOfLeave(RequestOfLeaveDto requestOfLeaveDto, String flag) {

        // This field generate in constructor i wrote here because common condition
        if (requestOfLeaveDto.getWantedTotalTime() != null) {
            throw new BadRequestException();
        }

        if (flag.equals("CREATE")) {
            // These fields generate in constructor, client can't send these parameters.
            if (requestOfLeaveDto.getId() != null
                    || requestOfLeaveDto.getStatus() != null
                    || requestOfLeaveDto.getRequestOpeningTime() != null) {
                throw new BadRequestException();
            }
            return new RequestOfLeave(
                    requestOfLeaveDto.getReason(),
                    requestOfLeaveDto.getStartDateOfLeave(),
                    requestOfLeaveDto.getEndDateOfLeave(),
                    requestOfLeaveDto.getLeaveType());
        }

        return new RequestOfLeave(
                requestOfLeaveDto.getId(),
                requestOfLeaveDto.getReason(),
                requestOfLeaveDto.getStartDateOfLeave(),
                requestOfLeaveDto.getEndDateOfLeave(),
                requestOfLeaveDto.getRequestOpeningTime(),
                requestOfLeaveDto.getLeaveType(),
                requestOfLeaveDto.getStatus());
    }

}