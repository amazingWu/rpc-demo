package xyz.amazingwu.blog.rpc.client;

import xyz.amazingwu.blog.rpc.common.Response;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年02月28日 14:41
 */
public class MessageFuture {
  private volatile boolean success = false;
  private Response response;
  private final Object lock = new Object();

  public Response getMessage() {
    synchronized (lock) {
      while (!success) {
        try {
          // 等待完成
          lock.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
      return response;
    }
  }

  public void setMessage(Response response) {
    synchronized (lock) {
      this.response = response;
      this.success = true;
      lock.notify();
    }
  }
}
