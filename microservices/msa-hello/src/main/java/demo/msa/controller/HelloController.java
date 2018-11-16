package demo.msa.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author anquan li
 */

@RestController
public class HelloController {

  @RequestMapping(method = RequestMethod.GET, path = "/hello")
  public String hello() {
    return "Hello Spring Boot";
  }
}
