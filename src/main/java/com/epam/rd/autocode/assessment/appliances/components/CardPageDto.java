package com.epam.rd.autocode.assessment.appliances.components;

import java.util.Set;

public record CardPageDto(
    Set<ApplianceInCardDto> appliances,
    String total) {

}
