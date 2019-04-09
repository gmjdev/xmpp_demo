package com.gm.apps.demo.web.controllers;

import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @GetMapping("/")
  public String ping() {
    return "Server Time: " + LocalDateTime.now().toString();
  }

}
