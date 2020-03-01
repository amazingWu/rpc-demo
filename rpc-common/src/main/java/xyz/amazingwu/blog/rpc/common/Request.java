package xyz.amazingwu.blog.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 请求传输体
 *
 * @author amazingjadewu@gmail.com
 * @created 2020年02月28日 11:41
 */
@Data
public class Request implements Serializable {
  private static final long serialVersionUID = 3933918042687238629L;
  private String className;
  private String methodName;
  private Class<?>[] paramTypes;
  private Object[] parameters;
}
