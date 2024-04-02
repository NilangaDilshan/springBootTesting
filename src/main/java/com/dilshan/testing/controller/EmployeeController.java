package com.dilshan.testing.controller;

import com.dilshan.testing.model.Employee;
import com.dilshan.testing.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        log.info("Create Employee: {}", employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.employeeService.saveEmployee(employee));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmpoloyees() {
        log.info("Get all employees...");
        return ResponseEntity.status(HttpStatus.OK).body(this.employeeService.getAllEmployees());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") Long id) {
        log.info("Get employee by id: {}", id);
        return this.employeeService.getEmployeeById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/id/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        log.info("Update employee by id: {} Employee: {}", id, employee);
        return this.employeeService.getEmployeeById(id).map(e -> {
            e.setLastName(employee.getLastName());
            e.setFirstName(employee.getFirstName());
            e.setEmail(employee.getEmail());
            Employee updatedEmployee = this.employeeService.updateEmployee(e);
            return new ResponseEntity<Employee>(updatedEmployee, HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity deleteEmployee(@PathVariable("id") Long id) {
        log.info("Delete employee by id: {}", id);
        return this.employeeService.getEmployeeById(id)
                .map(e -> {
                    this.employeeService.deleteEmployee(id);
                    return ResponseEntity.status(HttpStatus.OK).build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
