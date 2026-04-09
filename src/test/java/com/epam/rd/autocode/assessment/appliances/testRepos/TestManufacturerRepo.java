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

@SpringBootTest
public class TestManufacturerRepo {

  @Autowired
  private ManufacturerRepository repo;

  @Test
  void test_fetch_all() {
    List<Manufacturer> m = repo.findAll();
    assertEquals(m.size(), 7);
  }

  @Test
  void test_fetch_by_id() {
    long id = 2;

    Optional<Manufacturer> m = repo.findById(id);
    if (m.isPresent()) {
      assertEquals(m.get().getName(), "Dell");
    } else {
      assertFalse(true);
    }
  }
}
