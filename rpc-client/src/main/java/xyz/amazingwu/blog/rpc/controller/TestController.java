package xyz.amazingwu.blog.rpc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.amazingwu.blog.rpc.client.SendMessage;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年02月28日 18:05
 */
@RestController
public class TestController {
  @Autowired
  private SendMessage sendMessage;

  @RequestMapping("/hello")
  public String getName() {
    return sendMessage.sendName("hh");
  }
}
