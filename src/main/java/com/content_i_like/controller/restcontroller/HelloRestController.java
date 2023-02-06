package com.content_i_like.controller.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloRestController {

  @GetMapping("/hello")
  public String hello() {
    return "hello2";
  }
}
