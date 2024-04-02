package com.dilshan.testing.service.impl;

import com.dilshan.testing.exception.ResourceNotFound;
import com.dilshan.testing.model.Employee;
import com.dilshan.testing.repository.EmployeeRepository;
import com.dilshan.testing.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee) {
        log.info("Save Employee: {}", employee);
        Optional<Employee> existingEmployee = this.employeeRepository.findByEmail(employee.getEmail());
        if (existingEmployee.isPresent()) {
            log.info("Employee already exists with the email {}", employee.getEmail());
            throw new ResourceNotFound(String.format("Employee already exists with given email. %s", employee.getEmail()));
        }
        return this.employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        log.info("Get all employees...");
        return this.employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        log.info("Get employee by id: {}", id);
        /*this.employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound(String.format("Employee not exists for id. %d", id)));*/
        return this.employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        log.info("Update Employee: {}", employee);
        return this.employeeRepository.save(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        log.info("Delete Employee by id: {}", id);
        this.employeeRepository.deleteById(id);
    }
}
