package com.epam.rd.autocode.assessment.appliances.exceptions;

public class FlowException extends AppException {
  private static final long serialVersionUID = -2889607185988464072L;

  public FlowException(String message, Throwable cause) {
    super(message, cause);
  }

  public FlowException(String message) {
    super(message);
  }
}
