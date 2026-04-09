package com.epam.rd.autocode.assessment.appliances.formbuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.validation.FieldError;

public abstract class FormUtility {
  // _FillingForm getFormUtility(List<FieldError> errorValidation, Class<T>
  // configForm, T result, E entity);

  public static Map<String, String> getRejectedValue(List<FieldError> errorValidation) {
    return errorValidation != null
        ? errorValidation.stream()
            .collect(Collectors.toMap(fE -> fE.getField(),
                fE -> fE.getRejectedValue() != null ? fE.getRejectedValue().toString() : ""))
        : new HashMap<>();
  }

  public static Map<String, String> getValidationErrors(List<FieldError> errorValidation) {
    return errorValidation != null
        ? errorValidation.stream()
            .collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (msg1, msg2) -> msg1 + "; " + msg2))
        : new HashMap<>();
  }

}
