package xyz.amazingwu.blog.rpc.client;

import lombok.Data;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年02月28日 17:27
 */
@Data
public class RpcClientFactoryBean implements FactoryBean {
  @Autowired
  private RpcDynamicPro rpcDynamicPro;

  private Class<?> classType;

  public RpcClientFactoryBean(Class<?> classType) {
    this.classType = classType;
  }

  @Override
  public Object getObject() throws Exception {
    ClassLoader classLoader = classType.getClassLoader();
    Object object = Proxy.newProxyInstance(classLoader, new Class<?>[]{classType}, rpcDynamicPro);
    return object;
  }

  @Override
  public Class<?> getObjectType() {
    return this.classType;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }
}
