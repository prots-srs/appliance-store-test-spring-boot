package com.epam.rd.autocode.assessment.appliances.testRepos;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;

@SpringBootTest

public class TestOrderRepo {

  // @Autowired
  // private ApplianceInOrderRepository repo;

  // @Autowired
  // private ApplianceRepository appRepo;

  // private OrderRow orderRowEntity;

  // @BeforeEach
  // void init() {
  // orderRowEntity = new OrderRow();

  // Appliance appliance = appRepo.getReferenceById(2L);
  // orderRowEntity.setAppliance(appliance);
  // orderRowEntity.setNumber(7L);
  // orderRowEntity.setAmount(new BigDecimal("23.10"));
  // }

  // @Test
  // void testCreateEntity() {
  // assertNotNull(orderRowEntity);

  // OrderRow saved = repo.saveAndFlush(orderRowEntity);
  // assertNotNull(saved);

  // Optional<OrderRow> fetched = repo.findById(saved.getId());
  // assertTrue(fetched.isPresent());

  // if (fetched.isPresent()) {
  // OrderRow fetchedEntity = fetched.get();

  // assertEquals(fetchedEntity.getAppliance().getId(), 2L);
  // assertEquals(fetchedEntity.getNumber(), 7L);
  // assertEquals(fetchedEntity.getAmount(), new BigDecimal("23.10"));
  // }
  // }
}
