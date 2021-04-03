package team.changeservice.controller.v1;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import team.changeservice.model.ChangeFailureRate;
import team.changeservice.model.ChangeRequest;
import team.changeservice.service.ChangeRequestService;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;

@Validated
@RestController
@RequestMapping(value = "/api/v1/changerequest", produces = "application/json")
@Api
public class ChangeRequestControllerV1
    {

    private final ChangeRequestService changeRequestService;

    @Autowired
    public ChangeRequestControllerV1(ChangeRequestService changeRequestService)
        {
        this.changeRequestService = changeRequestService;
        }

        @PostMapping("")
        @ResponseStatus(HttpStatus.CREATED)
        @ApiOperation(value = "Store a list of change requests", notes = "Store a list of change requests", response = ChangeRequest.class, responseContainer = "List")
        public List<ChangeRequest> store(@RequestBody @NotEmpty(message = "Input change requests list cannot be empty.") List<@Valid ChangeRequest> changeRequests){
                List<ChangeRequest> output = new ArrayList<>();
                changeRequests.forEach(c -> output.add(changeRequestService.store(c)));
                return output;
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity handle(ConstraintViolationException constraintViolationException) {
            Set<ConstraintViolation<?>> violations = constraintViolationException.getConstraintViolations();
            String errorMessage = "";
            if (!violations.isEmpty()) {
                StringBuilder builder = new StringBuilder();
                violations.forEach(violation -> builder.append(" " + violation.getMessage()));
                errorMessage = builder.toString();
            } else {
                errorMessage = "ConstraintViolationException occurred.";
            }
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    
        @GetMapping("")
        @ResponseStatus(HttpStatus.OK)
        @ApiOperation(value = "List all change requests", notes = "List all change requests", response = ChangeRequest.class, responseContainer = "List")
        public List<ChangeRequest> list(){
            return changeRequestService.list();
        }

        @GetMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        @ApiOperation(value = "Get a specific change request specified by it's id", response = ChangeRequest.class)
        public Optional<ChangeRequest> show(@PathVariable @ApiParam(value = "The change request id", required = true) String id){
            return changeRequestService.get(id);
        }

        @DeleteMapping("/{id}")
        @ResponseStatus(HttpStatus.OK)
        @ApiOperation(value = "Delete a specific change request specified by it's id")
        public String delete(@PathVariable String id) { return changeRequestService.delete(id); }
    
        @GetMapping("/application/{id}")
        @ResponseStatus(HttpStatus.OK)
        @ApiOperation(value = "Get all change requests associated with an application id", response = ChangeRequest.class, responseContainer = "List")
        public List<ChangeRequest> listForApp(@PathVariable @ApiParam(value = "The application id", required = true) String id){
            return changeRequestService.listAllForApplication(id);
        }

        @GetMapping("/hierarchy/{id}")
        @ResponseStatus(HttpStatus.OK)
        @ApiOperation(value = "Get all change requests associated with a hierarchy", response = ChangeRequest.class, responseContainer = "List")
        public List<ChangeRequest> listForHierarchy(@PathVariable @ApiParam(value = "The application id", required = true) String id){
            return changeRequestService.listAllForHierarchy(id);
        }

        @GetMapping("/application/{id}/date/{date}")
        @ResponseStatus(HttpStatus.OK)
        @ApiOperation(value = "Get all change requests associated with an application id for a specific date", response = ChangeRequest.class, responseContainer = "List")
        public List<ChangeRequest> listForAppAndDate(@PathVariable @ApiParam(value = "The application id", required = true) String id, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "The change requests closed date in ISO Date format YYYY-MM-dd", required = true) LocalDate date){
            Date reportingDate = Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant());
            return changeRequestService.listAllForApplication(id, reportingDate);
        }

        @GetMapping("/application/{id}/cfr")
        @ResponseStatus(HttpStatus.OK)
        @ApiOperation(value = "Calculate change failure rate over the last 90 days for an application", response = ChangeRequest.class)
        public ChangeFailureRate calculateChangeFailureRate(@PathVariable @ApiParam(value = "The application id", required = true) String id){
            ZonedDateTime date = LocalDate.now().minusDays(1).atStartOfDay(ZoneOffset.UTC);        
            Date reportingDate = Date.from(date.toInstant());
            return changeRequestService.calculateChangeFailureRate(id, reportingDate);
        }

        @GetMapping("/application/{id}/cfr/{date}")
        @ResponseStatus(HttpStatus.OK)
        @ApiOperation(value = "Calculate change failure rate over 90 days for an application from a given date", response = ChangeRequest.class)
        public ChangeFailureRate calculateChangeFailureRate(@PathVariable @ApiParam(value = "The application id", required = true) String id, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @ApiParam(value = "The change requests closed date in ISO Date format YYYY-MM-dd", required = true) LocalDate date){
            Date reportingDate = Date.from(date.atStartOfDay(ZoneOffset.UTC).toInstant());
            return changeRequestService.calculateChangeFailureRate(id, reportingDate);
        }
    }
