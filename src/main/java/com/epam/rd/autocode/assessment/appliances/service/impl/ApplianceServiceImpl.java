package com.epam.rd.autocode.assessment.appliances.service.impl;

import java.lang.reflect.RecordComponent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.epam.rd.autocode.assessment.appliances.exceptions.InvalidParameterException;
import com.epam.rd.autocode.assessment.appliances.formbuilder.FormUtility;
import com.epam.rd.autocode.assessment.appliances.formbuilder.FormValuesDto;
import com.epam.rd.autocode.assessment.appliances.model.Appliance;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.model.OrderRow;
import com.epam.rd.autocode.assessment.appliances.panel.PanelService;
import com.epam.rd.autocode.assessment.appliances.panel.forms.results.ApplianceFormResult;
import com.epam.rd.autocode.assessment.appliances.panel.table.ApplianceViewDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.TableDto;
import com.epam.rd.autocode.assessment.appliances.repository.ApplianceRepository;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ApplianceServiceImpl implements PanelService<ApplianceViewDto, ApplianceFormResult> {

  private ApplianceRepository repo;
  private Locale locale;
  private ManufacturerRepository manufacturerRepo;

  public ApplianceServiceImpl(ApplianceRepository repo,
      ManufacturerRepository manufacturerRepo) {
    this.repo = repo;
    locale = LocaleContextHolder.getLocale();
    this.manufacturerRepo = manufacturerRepo;
  }

  @Override
  public TableDto<ApplianceViewDto> getTable(PaginationRequestDto page) {

    int currentPage = page.page() > 0 ? page.page() - 1 : 0;
    int showSize = page.size() > 0 ? page.size() : 3;

    Pageable pageable = page.sort() != null && !page.sort().isEmpty()
        ? PageRequest.of(currentPage, showSize, Sort.by(page.sort()).ascending())
        : PageRequest.of(currentPage, showSize, Sort.by("id").descending());

    Page<Appliance> resultQuery = repo.findAll(pageable);

    // map
    List<ApplianceViewDto> list = new ArrayList<>();
    resultQuery.getContent().forEach(e -> list.add(ApplianceViewDto.convertFromEntity(e, locale)));

    return new TableDto<ApplianceViewDto>(list,
        new PaginationDto(currentPage + 1,
            resultQuery.getTotalElements(),
            resultQuery.getTotalPages(),
            showSize));
  }

  @Override
  public FormValuesDto getForm(@Nullable Long id,
      @Nullable ApplianceFormResult item,
      @Nullable List<FieldError> fieldErrors) {

    return getFormUtility(item, getEntity(id), fieldErrors);
  }

  public Appliance getEntity(Long id) {

    Appliance entity = null;
    if (id != null && id > 0) {
      var entityOp = repo.findById(id);
      if (entityOp.isPresent()) {
        entity = entityOp.get();
      }
    }
    return entity;
  }

  @Override
  public Long create(ApplianceFormResult item) {
    if (item == null) {
      throw new InvalidParameterException("ApplianceFormResult is null");
    }

    Long result = 0L;

    Appliance entity = createEntity(item);
    if (entity != null) {
      Appliance saved = repo.saveAndFlush(entity);
      if (saved != null) {
        result = saved.getId();
      }
    }

    return result;
  }

  private Appliance createEntity(ApplianceFormResult item) {
    Appliance entity = new Appliance();
    entity.setName(item.name() != null ? item.name() : "");
    entity.setCategory(item.category() != null ? item.category() : null);
    entity.setModel(item.model() != null ? item.model() : "");

    if (item.manufacturer() != null) {
      Manufacturer manufacturerEntity = manufacturerRepo.getReferenceById(item.manufacturer());
      entity.setManufacturer(manufacturerEntity);
    }

    entity.setPowerType(item.powerType() != null ? item.powerType() : null);
    entity.setCharacteristic(item.characteristic() != null ? item.characteristic() : "");
    entity.setDescription(item.description() != null ? item.description() : "");
    entity.setPower(item.power() != null ? item.power() : 0);
    entity.setPrice(item.price() != null ? item.price() : BigDecimal.valueOf(0.0));

    return entity;
  }

  @Override
  public void update(Long id, ApplianceFormResult item) {
    if (id == null || item == null) {
      throw new InvalidParameterException("ApplianceFormResult and id are null");
    }

    Appliance entity = createEntity(item);
    entity.setId(id);

    repo.saveAndFlush(entity);
  }

  @Override
  public Boolean delete(Long id) {
    if (id == null) {
      throw new InvalidParameterException("Id is null");
    }

    if (repo.existsById(id)) {
      Optional<Appliance> entityOp = repo.findById(id);
      if (entityOp.isPresent()) {
        Appliance entity = entityOp.get();
        if (entity != null) {
          repo.delete(entity);
        }
        return true;
      }
    }

    return false;
  }

  private FormValuesDto getFormUtility(
      ApplianceFormResult result,
      Appliance entity,
      List<FieldError> errorValidation) {

    Map<String, String> values = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    // pressed
    Map<String, String> validationErrors = FormUtility.getValidationErrors(errorValidation);
    // Map<String, String> rejectedValue = getRejectedValue(errorValidation);

    RecordComponent[] recordFields = result != null ? result.getClass().getRecordComponents()
        : ApplianceFormResult.class.getRecordComponents();
    List<String> fieldNames = Arrays.stream(recordFields).map(f -> f.getName()).toList();

    fieldNames.stream().forEach(fieldName -> {
      errors.put(fieldName,
          validationErrors.containsKey(fieldName) ? validationErrors.get(fieldName) : "");

      // add empty values to new form
      values.put(fieldName, "");
    });

    setEntityValues(values, entity);
    setResultValues(values, result);

    return new FormValuesDto(
        entity != null ? entity.getId() : 0L,
        entity == null,
        values, errors);
  }

  private void setResultValues(Map<String, String> values, ApplianceFormResult result) {
    if (result == null) {
      return;
    }

    values.keySet().forEach(fieldName -> {
      values.put(fieldName, switch (fieldName) {
        case "name" -> result.name();
        case "category" -> result.category() != null ? result.category().toString() : "";
        case "model" -> result.model();
        case "powerType" -> result.powerType() != null ? result.powerType().toString() : "";
        case "manufacturer" -> result.manufacturer() != null ? result.manufacturer().toString() : "";
        case "characteristic" -> result.characteristic();
        case "description" -> result.description();
        case "power" -> result.power() != null ? result.power().toString() : "";
        case "price" -> result.price() != null ? result.price().toString() : "";
        default -> "";
      });
    });
  }

  private void setEntityValues(Map<String, String> values, Appliance entity) {

    if (entity == null) {
      return;
    }

    values.keySet().stream().forEach(fieldName -> {
      values.put(fieldName, switch (fieldName) {
        case "name" -> entity.getName();
        case "category" -> entity.getCategory() != null ? entity.getCategory().toString() : "";
        case "model" -> entity.getModel();
        case "powerType" -> entity.getPowerType() != null ? entity.getPowerType().toString() : "";
        case "manufacturer" -> entity.getManufacturer() != null ? entity.getManufacturer().getId().toString() : "";
        case "characteristic" -> entity.getCharacteristic();
        case "description" -> entity.getDescription();
        case "power" -> entity.getPower() != null ? entity.getPower().toString() : "";
        case "price" -> entity.getPrice() != null ? entity.getPrice().toString() : "";
        default -> "";
      });
    });
  }

}
