package com.epam.rd.autocode.assessment.appliances.panel;

import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;

import com.epam.rd.autocode.assessment.appliances.formbuilder.FormValuesDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.PaginationRequestDto;
import com.epam.rd.autocode.assessment.appliances.panel.table.TableDto;

// T is table row
// V is form result
public interface PanelService<T, V> {
  TableDto<T> getTable(PaginationRequestDto page);

  FormValuesDto getForm(
      @Nullable Long id,
      @Nullable V item,
      @Nullable List<FieldError> fieldErrors);

  Long create(V item);

  void update(Long id, V item);

  Boolean delete(Long id);
}
