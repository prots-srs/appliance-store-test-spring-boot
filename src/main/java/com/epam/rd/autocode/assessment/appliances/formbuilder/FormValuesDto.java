package com.epam.rd.autocode.assessment.appliances.formbuilder;

import java.util.Map;

public record FormValuesDto(
    Long id,
    Boolean isNew,
    Map<String, String> values,
    Map<String, String> errors) {

}
