package xyz.amazingwu.blog.rpc.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年02月28日 12:01
 */
@Data
public class Response implements Serializable {
  private static final long serialVersionUID = -2393333111247658778L;
  private Object result;
}
