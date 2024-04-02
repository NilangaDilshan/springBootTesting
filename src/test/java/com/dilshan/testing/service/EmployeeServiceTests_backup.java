package com.dilshan.testing.service;

import com.dilshan.testing.model.Employee;
import com.dilshan.testing.repository.EmployeeRepository;
import com.dilshan.testing.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Disabled
public class EmployeeServiceTests_backup {

    private EmployeeRepository employeeRepository;
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    //JUnit test for save employee method
    @DisplayName("JUnit test for save employee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .id(1L)
                .firstName("Dilshan")
                .lastName("Wijetunga")
                .email("test@gmail.com")
                .build();
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behaviour to be tested
        Employee savedEmployee = this.employeeService.saveEmployee(employee);

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
    }
}
