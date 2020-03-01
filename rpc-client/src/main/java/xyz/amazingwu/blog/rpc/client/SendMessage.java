package xyz.amazingwu.blog.rpc.client;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年02月28日 17:47
 */
@RpcClient
public interface SendMessage {
  /**
   * 定义rpc client
   *
   * @param name
   * @return
   */
  String sendName(String name);
}
