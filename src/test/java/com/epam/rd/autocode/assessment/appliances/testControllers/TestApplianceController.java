package com.epam.rd.autocode.assessment.appliances.testControllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import jakarta.transaction.Transactional;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
public class TestApplianceController {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ApplianceRepository repo;

  @Test
  @Transactional

  void testAddNewAppliance() throws Exception {
    ResultActions request = mockMvc.perform(
        post("/panel/appliances/create")
            // .with(user("admin"))
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "Lamp")
            .param("category", "SMALL")
            .param("model", "abc-model")
            .param("powerType", "ACCUMULATOR")
            .param("manufacturer", "2")
            .param("characteristic", "lighter")
            .param("description", "vertical")
            .param("power", "20")
            .param("price", "4.05"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/panel/appliances/8/edit"));

    String path = request.andReturn().getResponse().getHeader("location");
    assertNotNull(path);
    Long id = Long.valueOf(path.split("/")[3]);
    assertNotNull(id);

    Appliance entity = repo.getReferenceById(id);
    assertNotNull(entity);
    assertEquals(entity.getId(), 8L);
    assertEquals(entity.getName(), "Lamp");
    assertEquals(entity.getCategory(), Category.SMALL);
    assertEquals(entity.getModel(), "abc-model");
    assertEquals(entity.getPowerType(), PowerType.ACCUMULATOR);
    assertEquals(entity.getManufacturer().getName(), "Dell");
    assertEquals(entity.getCharacteristic(), "lighter");
    assertEquals(entity.getDescription(), "vertical");
    assertEquals(entity.getPower(), 20);
    assertEquals(entity.getPrice(), BigDecimal.valueOf(4.05));

  }

  @Transactional
  @Test
  void testEditAppliance() throws Exception {
    ResultActions request = mockMvc.perform(
        post("/panel/appliances/3/edit")
            // .with(user("admin"))
            .with(csrf())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "Lamp")
            .param("category", "SMALL")
            .param("model", "abc-model")
            .param("powerType", "ACCUMULATOR")
            .param("manufacturer", "2")
            .param("characteristic", "lighter")
            .param("description", "vertical")
            .param("power", "20")
            .param("price", "4.05"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/panel/appliances/3/edit"));

    String path = request.andReturn().getResponse().getHeader("location");
    assertNotNull(path);
    Long id = Long.valueOf(path.split("/")[3]);
    assertNotNull(id);

    Appliance entity = repo.getReferenceById(id);
    assertNotNull(entity);
    assertEquals(entity.getId(), 3L);
    assertEquals(entity.getName(), "Lamp");
    assertEquals(entity.getCategory(), Category.SMALL);
    assertEquals(entity.getModel(), "abc-model");
    assertEquals(entity.getPowerType(), PowerType.ACCUMULATOR);
    assertEquals(entity.getManufacturer().getName(), "Dell");
    assertEquals(entity.getCharacteristic(), "lighter");
    assertEquals(entity.getDescription(), "vertical");
    assertEquals(entity.getPower(), 20);
    assertEquals(entity.getPrice(), BigDecimal.valueOf(4.05));
  }

}
