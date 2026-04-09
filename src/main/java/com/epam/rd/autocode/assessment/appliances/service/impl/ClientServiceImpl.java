
package com.epam.rd.autocode.assessment.appliances.service.impl;

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
import com.epam.rd.autocode.assessment.appliances.model.Client;
import com.epam.rd.autocode.assessment.appliances.panel.PanelService;
import com.epam.rd.autocode.assessment.appliances.panel.forms.results.ClientFormResult;
import com.epam.rd.autocode.assessment.appliances.panel.table.ClientViewDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.TableDto;
import com.epam.rd.autocode.assessment.appliances.repository.ClientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ClientServiceImpl implements PanelService<ClientViewDto, ClientFormResult> {

  private ClientRepository repo;

  public ClientServiceImpl(ClientRepository repo) {
    this.repo = repo;
  }

  @Override
  public TableDto<ClientViewDto> getTable(PaginationRequestDto page) {

    int currentPage = page.page() > 0 ? page.page() - 1 : 0;
    int showSize = page.size() > 0 ? page.size() : 3;

    Pageable pageable = page.sort() != null && !page.sort().isEmpty()
        ? PageRequest.of(currentPage, showSize, Sort.by(page.sort()).ascending())
        : PageRequest.of(currentPage, showSize, Sort.by("id").descending());

    Page<Client> resultQuery = repo.findAll(pageable);

    // map
    List<ClientViewDto> list = new ArrayList<>();
    resultQuery.getContent().forEach(e -> list.add(ClientViewDto.convertFromEntity(e)));

    return new TableDto<ClientViewDto>(list,
        new PaginationDto(currentPage + 1,
            resultQuery.getTotalElements(),
            resultQuery.getTotalPages(),
            showSize));
  }

  @Override
  public FormValuesDto getForm(@Nullable Long id,
      @Nullable ClientFormResult item,
      @Nullable List<FieldError> fieldErrors) {

    return getFormUtility(item, getEntity(id), fieldErrors);
  }

  private Client getEntity(Long id) {

    Client entity = null;
    if (id != null && id > 0) {
      var entityOp = repo.findById(id);
      if (entityOp.isPresent()) {
        entity = entityOp.get();
      }
    }
    return entity;
  }

  @Override
  public Long create(ClientFormResult item) {
    if (item == null) {
      throw new InvalidParameterException("ClientFormResult is null");
    }

    Long result = 0L;

    Client entity = createEntity(item);
    if (entity != null) {
      Client saved = repo.saveAndFlush(entity);
      if (saved != null) {
        result = saved.getId();
      }
    }

    return result;
  }

  private Client createEntity(ClientFormResult item) {
    Client entity = new Client();
    entity.setName(item.name() != null ? item.name() : "");
    entity.setEmail(item.email() != null ? item.email() : "");
    entity.setPassword(item.password() != null ? item.password() : "");
    entity.setCard(item.card() != null ? item.card() : "");
    return entity;
  }

  @Override
  public void update(Long id, ClientFormResult item) {
    if (id == null || item == null) {
      throw new InvalidParameterException("ClientFormResult and id are null");
    }

    Client entity = createEntity(item);
    entity.setId(id);

    // set password, password only change
    if (entity.getPassword().isBlank()) {
      if (repo.existsById(id)) {
        Optional<Client> entityOp = repo.findById(id);
        if (entityOp.isPresent()) {
          Client entitySaved = entityOp.get();
          if (entitySaved != null) {
            entity.setPassword(entitySaved.getPassword());
          }
        }
      }
    }

    repo.saveAndFlush(entity);
  }

  @Override
  public Boolean delete(Long id) {
    if (id == null) {
      throw new InvalidParameterException("Id is null");
    }

    if (repo.existsById(id)) {
      Optional<Client> entityOp = repo.findById(id);
      if (entityOp.isPresent()) {
        Client entity = entityOp.get();
        if (entity != null) {
          repo.delete(entity);
        }
        return true;
      }
    }

    return false;
  }

  private FormValuesDto getFormUtility(
      ClientFormResult result,
      Client entity,
      List<FieldError> errorValidation) {

    Map<String, String> values = new HashMap<>();
    Map<String, String> errors = new HashMap<>();

    // pressed
    Map<String, String> validationErrors = FormUtility.getValidationErrors(errorValidation);
    // Map<String, String> rejectedValue = getRejectedValue(errorValidation);

    RecordComponent[] recordFields = result != null ? result.getClass().getRecordComponents()
        : ClientFormResult.class.getRecordComponents();
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

  private void setResultValues(Map<String, String> values, ClientFormResult result) {
    if (result == null) {
      return;
    }

    values.keySet().forEach(fieldName -> {
      values.put(fieldName, switch (fieldName) {
        case "name" -> result.name();
        case "email" -> result.email();
        case "card" -> result.card();
        default -> "";
      });
    });
  }

  private void setEntityValues(Map<String, String> values, Client entity) {

    if (entity == null) {
      return;
    }

    values.keySet().stream().forEach(fieldName -> {
      values.put(fieldName, switch (fieldName) {
        case "name" -> entity.getName();
        case "email" -> entity.getEmail();
        case "card" -> entity.getCard();
        default -> "";
      });
    });
  }
}
