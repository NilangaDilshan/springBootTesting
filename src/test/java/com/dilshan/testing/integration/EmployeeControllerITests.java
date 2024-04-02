package com.dilshan.testing.integration;

import com.dilshan.testing.model.Employee;
import com.dilshan.testing.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@AutoConfigureMockMvc
public class EmployeeControllerITests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        this.employeeRepository.deleteAll();
    }

    //Integration test for create employee method
    @DisplayName("Integration test for create employee method")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        //given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Dilshan")
                .lastName("Wije")
                .email("test@gmail.com")
                .build();


        //when - action or the behaviour to be tested
        ResultActions response = this.mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        //then -verify the output
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    //Integration test for get all employees api
    @DisplayName("Integration test for get all employees api")
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnAllEmployeesList() throws Exception {

        //given - precondition or setup
        List<Employee> employeeList = List.of(
                Employee.builder()
                        .firstName("Dilshan")
                        .lastName("Wije")
                        .email("test@gmail.com")
                        .build(),
                Employee.builder()
                        .firstName("Dimmu")
                        .lastName("Borgir")
                        .email("dimmu@gmail.com")
                        .build());
        this.employeeRepository.saveAll(employeeList);

        //when - action or the behaviour to be tested
        ResultActions response = this.mockMvc.perform(get("/api/employees/all")
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(2)));

    }

    //positive scenario - valid employee id
    //Integration test for get employee by id rest api
    @DisplayName("Integration test for get employee by id rest api")
    @Test
    public void givenId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {

        //given - precondition or setup
        Long employeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Dilshan")
                .lastName("Wije")
                .email("test@gmail.com")
                .build();
        this.employeeRepository.save(employee);

        //when - action or the behaviour to be tested
        ResultActions response = this.mockMvc.perform(get("/api/employees/id/{id}", employee.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    //negative scenario - invalid employee id
    //Integration test for get employee by id rest api with invalid id
    @DisplayName("Integration test for get employee by id rest api with invalid id")
    @Test
    public void givenInvalidId_whenGetEmployeeById_thenReturnEmpty() throws Exception {

        //given - precondition or setup
        Long invalidEmployeeId = 1L;
        Employee employee = Employee.builder()
                .firstName("Dilshan")
                .lastName("Wije")
                .email("test@gmail.com")
                .build();
        this.employeeRepository.save(employee);

        //when - action or the behaviour to be tested
        ResultActions response = this.mockMvc.perform(get("/api/employees/id/{id}", invalidEmployeeId)
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    //Integration test for update employee
    @DisplayName("Integration test for update employee")
    @Test
    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {

        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Dilshan")
                .lastName("Wije")
                .email("test@gmail.com")
                .build();
        Employee updateEmployee = Employee.builder()
                .firstName("Nilanga")
                .lastName("Loku")
                .email("loku@gmail.com")
                .build();
        this.employeeRepository.save(savedEmployee);

        //when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(put("/api/employees/id/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));
        //then -verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updateEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updateEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updateEmployee.getEmail())));
    }

    //Integration test for update employee negative scenario
    @DisplayName("Integration test for update employee")
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

        //when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(put("/api/employees/id/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateEmployee)));
        //then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    //Integration test for delete employee by id rest api
    @DisplayName("Integration test for delete employee by id rest api")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturnResponseStatus200() throws Exception {

        //given - precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Dilshan")
                .lastName("Wije")
                .email("test@gmail.com")
                .build();
        this.employeeRepository.save(savedEmployee);

        //when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(delete("/api/employees/id/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

    //Integration test for delete employee by id rest api negative scenario
    @DisplayName("Integration test for delete employee by id rest api")
    @Test
    public void givenInvalidEmployeeId_whenDeleteEmployee_thenReturnResponseStatus404() throws Exception {

        //given - precondition or setup
        Long employeeId = 1L;

        //when - action or the behaviour to be tested
        ResultActions response = mockMvc.perform(delete("/api/employees/id/{id}", employeeId)
                .contentType(MediaType.APPLICATION_JSON));

        //then -verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());
    }
}
