package com.epam.rd.autocode.assessment.appliances.service;

import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;

import com.epam.rd.autocode.assessment.appliances.exceptions.InvalidParameterException;
import com.epam.rd.autocode.assessment.appliances.formbuilder.FormUtility;
import com.epam.rd.autocode.assessment.appliances.formbuilder.FormValuesDto;
import com.epam.rd.autocode.assessment.appliances.model.Manufacturer;
import com.epam.rd.autocode.assessment.appliances.panel.PanelService;
import com.epam.rd.autocode.assessment.appliances.panel.forms.ManufacturerFormResult;
import com.epam.rd.autocode.assessment.appliances.panel.table.ManufacturerViewDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.TableDto;
import com.epam.rd.autocode.assessment.appliances.repository.ManufacturerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ManufacturerServiceImpl implements PanelService<ManufacturerViewDto, ManufacturerFormResult> {

  private ManufacturerRepository repo;

  public ManufacturerServiceImpl(ManufacturerRepository repo) {
    this.repo = repo;
  }

  @Override
  public TableDto<ManufacturerViewDto> getTable(PaginationRequestDto page) {

    int currentPage = page.page() > 0 ? page.page() - 1 : 0;
    int showSize = page.size() > 0 ? page.size() : 3;

    Pageable pageable = page.sort() != null && !page.sort().isEmpty()
        ? PageRequest.of(currentPage, showSize, Sort.by(page.sort()).ascending())
        : PageRequest.of(currentPage, showSize, Sort.by("id").descending());

    Page<Manufacturer> resultQuery = repo.findAll(pageable);

    // map
    List<ManufacturerViewDto> list = new ArrayList<>();
    resultQuery.getContent().forEach(e -> list.add(ManufacturerViewDto.convertFromEntity(e)));

    return new TableDto<ManufacturerViewDto>(list,
        new PaginationDto(currentPage + 1,
            resultQuery.getTotalElements(),
            resultQuery.getTotalPages(),
            showSize));
  }

  @Override
  public FormValuesDto getForm(@Nullable Long id,
      @Nullable ManufacturerFormResult item,
      @Nullable List<FieldError> fieldErrors) {

    return getFormUtility(item, getEntity(id), fieldErrors);
  }

  private Manufacturer getEntity(Long id) {

    Manufacturer entity = null;
    if (id != null && id > 0) {
      var entityOp = repo.findById(id);
      if (entityOp.isPresent()) {
        entity = entityOp.get();
      }
    }
    return entity;
  }

  @Override
  public Long create(ManufacturerFormResult item) {
    if (item == null) {
      throw new InvalidParameterException("ManufacturerFormResult is null");
    }

    Long result = 0L;

    Manufacturer entity = createEntity(item);
    if (entity != null) {
      Manufacturer saved = repo.saveAndFlush(entity);
      if (saved != null) {
        result = saved.getId();
      }
    }

    return result;
  }

  private Manufacturer createEntity(ManufacturerFormResult item) {
    Manufacturer entity = new Manufacturer();
    entity.setName(item.name() != null ? item.name() : "");
    return entity;
  }

  @Override
  public void update(Long id, ManufacturerFormResult item) {
    if (id == null || item == null) {
      throw new InvalidParameterException("ManufacturerFormResult and id are null");
    }

    Manufacturer entity = createEntity(item);
    entity.setId(id);

    repo.saveAndFlush(entity);
  }

  @Override
  public Boolean delete(Long id) {
    if (id == null) {
      throw new InvalidParameterException("Id is null");
    }

    if (repo.existsById(id)) {
      Optional<Manufacturer> entityOp = repo.findById(id);
      if (entityOp.isPresent()) {
        Manufacturer entity = entityOp.get();
        if (entity != null) {
          repo.delete(entity);
        }
        return true;
      }
    }

    return false;
  }

  private FormValuesDto getFormUtility(
      ManufacturerFormResult result,
      Manufacturer entity,
      List<FieldError> errorValidation) {

    Map<String, String> values = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    // pressed
    Map<String, String> validationErrors = FormUtility.getValidationErrors(errorValidation);
    // Map<String, String> rejectedValue = getRejectedValue(errorValidation);

    RecordComponent[] recordFields = result != null ? result.getClass().getRecordComponents()
        : ManufacturerFormResult.class.getRecordComponents();
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

  private void setResultValues(Map<String, String> values, ManufacturerFormResult result) {
    if (result == null) {
      return;
    }

    values.keySet().forEach(fieldName -> {
      values.put(fieldName, switch (fieldName) {
        case "name" -> result.name();
        default -> "";
      });
    });
  }

  private void setEntityValues(Map<String, String> values, Manufacturer entity) {

    if (entity == null) {
      return;
    }

    values.keySet().stream().forEach(fieldName -> {
      values.put(fieldName, switch (fieldName) {
        case "name" -> entity.getName();
        default -> "";
      });
    });
  }

  public List<ManufacturerViewDto> getListForOptions(Sort sort) {
    List<ManufacturerViewDto> result = new ArrayList<>();
    if (sort != null) {
      List<Manufacturer> list = repo.findAll(sort);
      list.forEach(e -> result.add(ManufacturerViewDto.convertFromEntity(e)));
    }

    return result;
  }
}
