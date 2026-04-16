package com.epam.rd.autocode.assessment.appliances.components;

public record ApplianceInCardDto(
    Long id,
    String name,
    String model,
    String price,
    Long quantity,
    String amount) {

}
