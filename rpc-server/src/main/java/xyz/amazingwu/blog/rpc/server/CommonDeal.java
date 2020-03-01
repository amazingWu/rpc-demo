package xyz.amazingwu.blog.rpc.server;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import xyz.amazingwu.blog.rpc.common.Request;
import xyz.amazingwu.blog.rpc.common.Response;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年03月01日 17:39
 */
@Slf4j
public class CommonDeal {

  public static String getInvokeMethodMes(String str){
    Request request = JSON.parseObject(str, Request.class);
    return JSON.toJSONString(invokeMethod(request));
  }

  private static Response invokeMethod(Request request) {
    String className = request.getClassName();
    String methodName = request.getMethodName();
    Object[] parameters = request.getParameters();
    Class<?>[] paramTypes = request.getParamTypes();
    Object o = InitRpcConfig.rpcServiceMap.get(className);
    Response response = new Response();
    try {
      Method method = o.getClass().getDeclaredMethod(methodName, paramTypes);
      Object invokeMethod = method.invoke(o, parameters);
      response.setResult(invokeMethod);
    } catch (NoSuchMethodException e) {
      log.info("没有找到" + methodName);
    } catch (IllegalAccessException e) {
      log.info("执行错误" + parameters);
    } catch (InvocationTargetException e) {
      log.info("执行错误" + parameters);
    }
    return response;
  }
}
