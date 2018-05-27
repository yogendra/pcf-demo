package me.yogendra.pcfdemo;

import java.util.Date;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {
  @RequestMapping("/greetings")
  public String greetings(@RequestParam(required = false, defaultValue = "Human", name = "name") String name){
    return String.format("Hello, %1$s. It's %2$tc here.", name, new Date());
  }
}
