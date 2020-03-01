package xyz.amazingwu.blog.rpc.server;

import org.springframework.stereotype.Service;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年03月01日 17:43
 */
@Service
public class SendMessageImpl implements SendMessage {
  @Override
  public String sendName(String name) {
    System.out.println("收到服务请求," + name);
    return String.valueOf(System.currentTimeMillis());
  }
}
