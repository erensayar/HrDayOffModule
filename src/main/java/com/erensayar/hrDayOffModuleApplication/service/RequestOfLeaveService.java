package com.erensayar.hrDayOffModuleApplication.service;

import com.erensayar.hrDayOffModuleApplication.model.dto.RequestOfLeaveDto;
import com.erensayar.hrDayOffModuleApplication.model.entity.RequestOfLeave;

import java.util.List;

public interface RequestOfLeaveService {

    RequestOfLeave createRequestOfLeave(RequestOfLeaveDto requestOfLeaveDto);

    RequestOfLeave getRequestOfLeaveById(Long id);

    List<RequestOfLeave> getRequestOfLeaveList();

    RequestOfLeave updateRequestOfLeave(RequestOfLeaveDto requestOfLeaveDto);

    void deleteRequestOfLeaveById(Long requestId, String employeeId);

    RequestOfLeave setStatusOfRequest(String employeeId, Long requestId);

}
