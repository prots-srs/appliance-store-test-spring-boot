package com.epam.rd.autocode.assessment.appliances.exceptions;

import java.util.StringJoiner;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.epam.rd.autocode.assessment.appliances.utils.LocalizeService;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ControllerAdvice
public class GlobalExceptionHandlerController {

  private LocalizeService localizeService;

  public GlobalExceptionHandlerController(LocalizeService localizeService) {
    this.localizeService = localizeService;
  }

  private static final Logger LOGGER = LogManager.getLogger();

  @ExceptionHandler(Throwable.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public String internalError(final Throwable throwable, final Model model,
      HttpServletRequest request) {

    StringJoiner st = new StringJoiner(", ");
    st.add(localizeService.getMessage("error.500"));
    if (throwable != null) {
      st.add(throwable.getMessage());
      throwable.printStackTrace();
    }
    model.addAttribute("message", st.toString());

    LOGGER.debug("GlobalException: Internal server error: {} {}", (throwable != null) ? throwable.getMessage() : "?",
        request.getRequestURL());
    return "error";
  }

  // @ExceptionHandler(IllegalArgumentException.class)
  @ExceptionHandler
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public String badRequest(final IllegalArgumentException ex, final Model model, HttpServletRequest request) {
    StringJoiner st = new StringJoiner(", ");
    st.add(localizeService.getMessage("error.400"));
    if (ex != null) {
      st.add(ex.getMessage());
    }
    model.addAttribute("message", st.toString());

    LOGGER.debug("GlobalException: Bad request {}", request.getRequestURL());
    return "error";
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public String noFoundError(final Model model, HttpServletRequest request) {
    model.addAttribute("message", localizeService.getMessage("error.404"));

    // TODO - write to file IP:URL

    LOGGER.debug("GlobalException: Not found: {}", request.getRequestURL());
    return "error";
  }

  @ExceptionHandler(MultipartException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handleMultipartException(MultipartException ex,
      HttpServletRequest request, Model model) {

    // ex.printStackTrace();

    model.addAttribute("message", "Invalid multipart request: " +
        ex.getMessage());
    return "error";
  }
}
