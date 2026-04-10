package com.epam.rd.autocode.assessment.appliances.testRepos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;
import org.springframework.data.domain.Sort;

@SpringBootTest
public class TestManufacturerRepo {

  @Autowired
  private ManufacturerRepository repo;

  @Test
  void testFetchAll() {
    Sort sort = Sort.by("name").ascending();
    List<Manufacturer> m = repo.findAll(sort);
    assertEquals(m.size(), 7);
    assertEquals(m.get(0).getName(), "AMD");
  }

  @Test
  void testFetchById() {
    long id = 2;

    Optional<Manufacturer> m = repo.findById(id);
    if (m.isPresent()) {
      assertEquals(m.get().getName(), "Dell");
    } else {
      assertFalse(true);
    }
  }
}
