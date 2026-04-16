package com.epam.rd.autocode.assessment.appliances.testControllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.epam.rd.autocode.assessment.appliances.model.Employee;
import com.epam.rd.autocode.assessment.appliances.repository.EmployeeRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class TestEmployeeController {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private EmployeeRepository repo;

  @Test
  void testAddNewEmployee() throws Exception {
    ResultActions request = mockMvc.perform(
        post("/panel/employees/create")
            // .with(user("admin"))
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "John")
            .param("email", "john@domain.com")
            .param("password", "123456")
            .param("department", "sociotropic"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/panel/employees/5/edit"));

    String path = request.andReturn().getResponse().getHeader("location");
    Long id = Long.valueOf(path.split("/")[3]);

    Employee entity = getEntity(id);

    assertNotNull(entity);
    assertEquals(entity.getName(), "John");
    assertEquals(entity.getEmail(), "john@domain.com");
    assertEquals(entity.getPassword(), "123456");
    assertEquals(entity.getDepartment(), "sociotropic");

  }

  @Test
  void testEditEmployee() throws Exception {
    ResultActions request = mockMvc.perform(
        post("/panel/employees/1/edit")
            // .with(user("admin"))
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "John")
            .param("email", "john@domain.com")
            .param("password", "123456")
            .param("department", "sociotropic"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/panel/employees/1/edit"));

    String path = request.andReturn().getResponse().getHeader("location");
    Long id = Long.valueOf(path.split("/")[3]);

    Employee entity = getEntity(id);

    assertNotNull(entity);
    assertEquals(entity.getName(), "John");
    assertEquals(entity.getEmail(), "john@domain.com");
    assertEquals(entity.getPassword(), "123456");
    assertEquals(entity.getDepartment(), "sociotropic");
  }

  private Employee getEntity(Long id) {

    Employee entity = null;
    if (id != null && id > 0) {
      var entityOp = repo.findById(id);
      if (entityOp.isPresent()) {
        entity = entityOp.get();
      }
    }
    return entity;
  }

}
