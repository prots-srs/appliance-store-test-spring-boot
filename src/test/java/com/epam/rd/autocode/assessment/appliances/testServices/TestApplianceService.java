package com.epam.rd.autocode.assessment.appliances.testServices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Answers.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.epam.rd.autocode.assessment.appliances.formbuilder.FormValuesDto;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Category;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.PowerType;
import com.epam.rd.autocode.assessment.appliances.panel.forms.results.ApplianceFormResult;
import com.epam.rd.autocode.assessment.appliances.panel.table.ApplianceViewDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.TableDto;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.service.impl.ApplianceServiceImpl;

import jakarta.transaction.Transactional;

@SpringBootTest
public class TestApplianceService {
  @Autowired
  private ApplianceServiceImpl applianceService;

  @Autowired
  private ApplianceRepository repo;

  @Test
  void testFetchPageService() {
    TableDto<ApplianceViewDto> m = applianceService.getTable(new PaginationRequestDto(0, 5, null));

    // m.list().forEach(d -> System.out.println("d:" + d.id() + " " + d.name()));
    // System.out.println("p:" + m.pagination().toString());

    assertEquals(m.list().size(), 5);
  }

  @Test
  void testGetNewFormValues() {
    FormValuesDto values = applianceService.getForm(null, null, null);

    assertNotNull(values);
    assertEquals(values.id(), 0L);
    assertTrue(values.isNew());
    assertTrue(values.values().containsKey("name"));

  }

  @Test
  void testGetExistFormValues() {
    FormValuesDto values = applianceService.getForm(2L, null, null);

    // System.out.println("z:" + values);
    assertNotNull(values);
    assertEquals(values.id(), 2L);
    assertFalse(values.isNew());
    assertTrue(values.values().containsKey("name"));
    assertEquals(values.values().get("name"), "Bane");

  }

  @Transactional
  @Test
  void testUpdateCurrentEntity() {
    ApplianceFormResult result = new ApplianceFormResult(
        "Lamp",
        Category.SMALL,
        "abc-model",
        PowerType.ACCUMULATOR,
        Long.valueOf(2L),
        "lighter",
        "vertical",
        Integer.valueOf(20),
        BigDecimal.valueOf(4.05));

    applianceService.update(3L, result);

    Appliance entity = repo.getReferenceById(3L);
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
