package com.epam.rd.autocode.assessment.appliances.testServices;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.epam.rd.autocode.assessment.appliances.formbuilder.FormValuesDto;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.panel.table.ManufacturerViewDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.TableDto;
import com.epam.rd.autocode.assessment.appliances.service.ManufacturerServiceImpl;

@SpringBootTest
public class TestManufacturerService {

  @Autowired
  private ManufacturerServiceImpl manufacturerService;

  @Test
  void test_fetch_page_service_manufacturer() {
    TableDto<ManufacturerViewDto> m = manufacturerService.getTable(new PaginationRequestDto(0, 5, null));

    // m.list().forEach(d -> System.out.println("d:" + d.id() + " " + d.name()));
    // System.out.println("p:" + m.pagination().toString());

    assertEquals(m.list().size(), 5);
  }

  @Test
  void testCreateEmptyForm() {
    FormValuesDto values = manufacturerService.getForm(null, null, null);

    assertNotNull(values);
    assertEquals(values.id(), 0L);
    assertTrue(values.isNew());
    assertTrue(values.values().containsKey("name"));

  }

  @Test
  void testCreateExistForm() {
    FormValuesDto values = manufacturerService.getForm(1L, null, null);

    // System.out.println("z:" + values);
    assertNotNull(values);
    assertEquals(values.id(), 1L);
    assertFalse(values.isNew());
    assertTrue(values.values().containsKey("name"));
    assertEquals(values.values().get("name"), "Samsung");

  }
}
