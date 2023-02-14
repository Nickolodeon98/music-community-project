package com.content_i_like.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorControllerAdvice {

  @ExceptionHandler(Exception.class)
  public String handleException(HttpServletRequest request, Exception ex, Model model) {
    // Store the previous page URL in the session
    String referrer = request.getHeader("referer");
    if (referrer != null) {
      request.getSession().setAttribute("prevPage", referrer);
    }

    model.addAttribute("prevPage", referrer);


    // Handle the exception and display an error page
    return "error";
  }
}
