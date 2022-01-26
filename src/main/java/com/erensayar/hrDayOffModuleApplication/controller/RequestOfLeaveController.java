package com.erensayar.hrDayOffModuleApplication.controller;

import com.erensayar.hrDayOffModuleApplication.model.dto.RequestOfLeaveDto;
import com.erensayar.hrDayOffModuleApplication.service.RequestOfLeaveService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "Request Of Leave Rest API Documentation")
@RequestMapping("/api/v1/requestOfLeaves")
@RequiredArgsConstructor
@RestController
public class RequestOfLeaveController {

    private final RequestOfLeaveService requestOfLeaveService;

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRequestOfLeaveById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(requestOfLeaveService.getRequestOfLeaveById(id), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getRequestOfLeaves() {
        return new ResponseEntity<>(requestOfLeaveService.getRequestOfLeaveList(), HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createRequestOfLeave(@RequestBody RequestOfLeaveDto RequestOfLeaveDto) {
        return new ResponseEntity<>(requestOfLeaveService.createRequestOfLeave(RequestOfLeaveDto), HttpStatus.CREATED);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateRequestOfLeave(@RequestBody RequestOfLeaveDto updatedRequestOfLeaveDto) {
        return new ResponseEntity<>(requestOfLeaveService.updateRequestOfLeave(updatedRequestOfLeaveDto), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{requestId}/{employeeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteRequestOfLeaveById(@PathVariable("requestId") Long requestId,
                                                      @PathVariable("employeeId") String employeeId) {
        requestOfLeaveService.deleteRequestOfLeaveById(requestId, employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/setStatusOfRequest/{employeeId}/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setStatusOfRequest(@PathVariable("employeeId") String employeeId,
                                                @PathVariable("requestId") Long requestId) {
        return new ResponseEntity<>(requestOfLeaveService.setStatusOfRequest(employeeId, requestId), HttpStatus.OK);
    }

}
