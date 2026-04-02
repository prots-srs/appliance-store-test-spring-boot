package com.epam.rd.autocode.assessment.appliances.exceptions;

public abstract class AppException extends RuntimeException {
  private static final long serialVersionUID = 78375014958540980L;

  public AppException(String message, Throwable cause) {
    super(message, cause);
  }

  public AppException(String message) {
    super(message);
  }

}
