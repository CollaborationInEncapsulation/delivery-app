package delivery.app.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UIController {

  @RequestMapping("/")
  public String root() {
    return "redirect:/index";
  }

  @RequestMapping("/index")
  public String index() {
    return "index";
  }

  @RequestMapping("/login")
  public String login() {
    return "login";
  }

}