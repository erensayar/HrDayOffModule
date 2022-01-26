package com.erensayar.hrDayOffModuleApplication.service.implementation;

import com.erensayar.hrDayOffModuleApplication.error.exception.BadRequestException;
import com.erensayar.hrDayOffModuleApplication.error.exception.NoContentException;
import com.erensayar.hrDayOffModuleApplication.model.dto.EmployeeDto;
import com.erensayar.hrDayOffModuleApplication.model.entity.Employee;
import com.erensayar.hrDayOffModuleApplication.model.entity.RequestOfLeave;
import com.erensayar.hrDayOffModuleApplication.repo.EmployeeRepo;
import com.erensayar.hrDayOffModuleApplication.service.EmployeeService;
import com.erensayar.hrDayOffModuleApplication.service.RequestOfLeaveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepo employeeRepo;
    private RequestOfLeaveService requestOfLeaveService;

    @Autowired // Setter injection used because EmployeeService and RequestOfLeaveService are nested.
    public void setRequestOfLeaveService(RequestOfLeaveService requestOfLeaveService) {
        this.requestOfLeaveService = requestOfLeaveService;
    }

    @Value("${company.right-of-leave.lessThen1Year}")
    private Double lessThen1Year;

    @Override
    public Employee createEmployee(EmployeeDto employeeDto) {
        if (employeeDto.getId() != null) {
            throw new BadRequestException();
        }
        employeeDto.setUnusedDayOff(lessThen1Year);
        employeeDto.setId("emp-" + UUID.randomUUID());
        return employeeRepo.save(converterOfEmployee(employeeDto));
    }

    @Override
    public Employee getEmployeeById(String id) {
        return employeeRepo.findById(id).orElseThrow(NoContentException::new);
    }

    @Override
    public List<Employee> getEmployees() {
        return employeeRepo.findAll();
    }

    @Override
    public Employee updateEmployee(EmployeeDto employeeDto) {
        if (employeeDto.getId() == null) {
            throw new BadRequestException();
        }
        this.getEmployeeById(employeeDto.getId()); // if employee doesn't exist then we throw an error from getEmployeeById method. if exist then can execute down line
        return employeeRepo.save(converterOfEmployee(employeeDto));
    }

    @Override
    public void deleteEmployeeById(String id) {
        this.getEmployeeById(id);
        employeeRepo.deleteById(id);
    }

    private Employee converterOfEmployee(EmployeeDto employeeDto) {

        // This block wrote for getting requestOfLeave object, through sent id numbers from client
        //<============================================================================================================>
        List<RequestOfLeave> requests = new ArrayList<>();
        if (employeeDto.getRequestOfLeaveIdNumbers() != null) {
            for (Long requestsIdNumber : employeeDto.getRequestOfLeaveIdNumbers()) {
                requests.add(requestOfLeaveService.getRequestOfLeaveById(requestsIdNumber));
            }
        }
        //<============================================================================================================>

        return Employee.builder()
                .id(employeeDto.getId())
                .name(employeeDto.getName())
                .surname(employeeDto.getSurname())
                .phoneNumber(employeeDto.getPhoneNumber())
                .companyMail(employeeDto.getCompanyMail())
                .personalMail(employeeDto.getPersonalMail())
                .unusedDayOff(employeeDto.getUnusedDayOff())
                .startDateOfWork(employeeDto.getStartDateOfWork())
                .requests(requests)
                .build();
    }
}
