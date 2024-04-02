package com.dilshan.testing.service;

import com.dilshan.testing.exception.ResourceNotFound;
import com.dilshan.testing.model.Employee;
import com.dilshan.testing.repository.EmployeeRepository;
import com.dilshan.testing.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.BDDMockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Dilshan")
                .lastName("Wijetunga")
                .email("test@gmail.com")
                .build();
    }

    //JUnit test for save employee method
    @DisplayName("JUnit test for save employee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        given(employeeRepository.save(employee)).willReturn(employee);

        //when - action or the behaviour to be tested
        Employee savedEmployee = this.employeeService.saveEmployee(employee);

        //then -verify the output
        assertThat(savedEmployee).isNotNull();
    }

    //JUnit test for save employee method which throws exception
    @DisplayName("JUnit test for save employee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {

        //given - precondition or setup
        given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        //when - action or the behaviour to be tested
        assertThrows(ResourceNotFound.class, () -> this.employeeService.saveEmployee(employee));

        //then
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    //JUnit test for get all employees
    @DisplayName("JUnit test for get all employees")
    @Test
    public void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {

        //given - precondition or setup
        Employee employee1 = Employee.builder()
                .id(1L)
                .firstName("Slayer")
                .lastName("Araya")
                .email("666@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        //when - action or the behaviour to be tested
        List<Employee> employeeList = this.employeeService.getAllEmployees();

        //then -verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    //JUnit test for get all employees (negative scenario)
    @DisplayName("JUnit test for get all employees (negative scenario)")
    @Test
    public void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {

        //given - precondition or setup
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        //when - action or the behaviour to be tested
        List<Employee> employeeList = this.employeeService.getAllEmployees();

        //then -verify the output
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    //JUnit test for get employee by id
    @DisplayName("JUnit test for get employee by id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {

        //given - precondition or setup
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when - action or the behaviour to be tested
        Optional<Employee> savedEmployee = this.employeeService.getEmployeeById(1L);

        //then -verify the output
        assertThat(savedEmployee.isPresent()).isTrue();
    }

    //JUnit test for update employee method
    @DisplayName("JUnit test for update employee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        //given - precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("slayer@gmail.com");
        employee.setFirstName("Dimmu");
        employee.setLastName("Borgir");

        //when - action or the behaviour to be tested
        Employee updatedEmployee = this.employeeService.updateEmployee(employee);

        //then -verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo("slayer@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Dimmu");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Borgir");
    }

    //JUnit test for delete employee method
    @DisplayName("JUnit test for delete employee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployeeMethod_thenNothing() {
        //given - precondition or setup
        Long employeeId = 1L;
        willDoNothing().given(this.employeeRepository).deleteById(employeeId);

        //when - action or the behaviour to be tested
        employeeService.deleteEmployee(employeeId);

        //then -verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);

    }
}
