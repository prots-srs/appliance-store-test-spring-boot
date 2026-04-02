package com.epam.rd.autocode.assessment.appliances.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "appliance")
@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public class Appliance extends BaseEntity {
  private String name;

  @Enumerated(EnumType.STRING)
  private Category category;
  private String model;

  @ManyToOne
  @JoinColumn(name = "manufacturer_id")
  private Manufacturer manufacturer;

  @Enumerated(EnumType.STRING)
  @Column(name = "power_type")
  private PowerType powerType;

  private String characteristic;
  private String description;
  private Integer power;
  private BigDecimal price;

}
