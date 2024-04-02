package com.dilshan.testing.controller;

import com.dilshan.testing.model.Employee;
import com.dilshan.testing.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.mockito.BDDMockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;

import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@WebMvcTest
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    //JUnit test for create employee method
    @DisplayName("JUnit test for create employee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Dilshan")
                .lastName("Wije")
                .email("test@gmail.com")
                .build();
        given(this.employeeService.saveEmployee((any(Employee.class)))).willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour to be tested
        employee.setId(1L);
        ResultActions response = this.mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then -verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andExpect(jsonPath("$.id", is(employee.getId().intValue())));
    }

    //JUnit test for get all employees api
    @DisplayName("JUnit test for get all employees api")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnAllEmployeesList() throws Exception {

        //given - precondition or setup
        List<Employee> employeeList = List.of(
                Employee.builder()
                        .id(1L)
                        .firstName("Dilshan")
                        .lastName("Wije")
                        .email("test@gmail.com")
                        .build(),
                Employee.builder()
                        .firstName("Dimmu")
                        .lastName("Borgir")
                        .email("dimmu@gmail.com")
                        .build());
        given(this.employeeService.getAllEmployees()).willReturn(employeeList);

        //when - action or the behaviour to be tested
        ResultActions response = this.mockMvc.perform(get("/api/employees/all")
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(2)));

    }

    //positive scenario - valid employee id
    //JUnit test for get employee by id rest api
    @DisplayName("JUnit test for get employee by id rest api")
    @Test
    public void givenId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .id(employeeId)
                .firstName("Dilshan")
                .lastName("Wije")
                .email("test@gmail.com")
                .build();
        given(this.employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));

        //when - action or the behaviour to be tested
        ResultActions response = this.mockMvc.perform(get("/api/employees/id/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())))
                .andExpect(jsonPath("$.id", is(employee.getId().intValue())));

    }

    //negative scenario - invalid employee id
    //JUnit test for get employee by id rest api with invalid id
    @DisplayName("JUnit test for get employee by id rest api with invalid id")
    @Test
    public void givenInvalidId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        Long employeeId = 1L;
        Long invalidEmployeeId = 2L;
        given(this.employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or the behaviour to be tested
        ResultActions response = this.mockMvc.perform(get("/api/employees/id/{id}", invalidEmployeeId)
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    //JUnit test for update employee
    @DisplayName("JUnit test for update employee")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        //given - precondition or setup
        Long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("Dilshan")
                .lastName("Wije")
                .email("test@gmail.com")
                .build();
        Employee updateEmployee = Employee.builder()
                .id(employeeId)
                .firstName("Nilanga")
                .lastName("Loku")
                .email("loku@gmail.com")
                .build();
        given(this.employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        given(this.employeeService.updateEmployee((any(Employee.class)))).willAnswer((invocation) -> invocation.getArgument(0));

        //when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(put("/api/employees/id/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));
        //then -verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updateEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updateEmployee.getEmail())))
                .andExpect(jsonPath("$.id", is(updateEmployee.getId().intValue())));
    }

    //JUnit test for update employee negative scenario
    @DisplayName("JUnit test for update employee")
    @Test
    public void givenInvalidIdOfUpdatedEmployee_whenUpdateEmployee_thenReturnStatusCode404() throws Exception {

        //given - precondition or setup
        Long employeeId = 1L;
        Employee updateEmployee = Employee.builder()
                .id(employeeId)
                .firstName("Nilanga")
                .lastName("Loku")
                .email("loku@gmail.com")
                .build();
        given(this.employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());

        //when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(put("/api/employees/id/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));
        //then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //JUnit test for delete employee by id rest api
    @DisplayName("JUnit test for delete employee by id rest api")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnResponseStatus200() throws Exception {

        //given - precondition or setup
        Long employeeId = 1L;
        Employee savedEmployee = Employee.builder()
                .id(employeeId)
                .firstName("Dilshan")
                .lastName("Wije")
                .email("test@gmail.com")
                .build();
        given(this.employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
        willDoNothing().given(this.employeeService).deleteEmployee(employeeId);

        //when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(delete("/api/employees/id/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

    //JUnit test for delete employee by id rest api negative scenario
    @DisplayName("JUnit test for delete employee by id rest api")
    @Test
    public void givenInvalidEmployeeId_whenDeleteEmployee_thenReturnResponseStatus404() throws Exception {

        //given - precondition or setup
        Long employeeId = 1L;
        given(this.employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
        willDoNothing().given(this.employeeService).deleteEmployee(employeeId);

        //when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(delete("/api/employees/id/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}
