package xyz.amazingwu.blog.rpc.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年03月01日 17:27
 */
@Component
@Slf4j
public class InitRpcConfig implements CommandLineRunner {

  @Autowired
  private ApplicationContext applicationContext;

  public static Map<String, Object> rpcServiceMap = new HashMap<>();

  @Override
  public void run(String... args) throws Exception {
    Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(Service.class);
    for (Object bean : beansWithAnnotation.values()) {
      Class<?> clazz = bean.getClass();
      Class<?>[] interfaces = clazz.getInterfaces();
      for (Class<?> inter : interfaces) {
        rpcServiceMap.put(getClassName(inter.getName()), bean);
        log.info("已经加载服务：{}", inter.getName());
      }
    }
    startPort();
  }

  private String getClassName(String beanClassName) {
    String className = beanClassName.substring(beanClassName.lastIndexOf(".") + 1);
    className = className.substring(0, 1).toLowerCase() + className.substring(1);
    return className;
  }

  public void startPort() throws IOException {
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    NioEventLoopGroup boos = new NioEventLoopGroup();
    NioEventLoopGroup worker = new NioEventLoopGroup();
    serverBootstrap
        .group(boos, worker)
        .channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<NioSocketChannel>() {
          @Override
          protected void initChannel(NioSocketChannel ch) {
            ch.pipeline().addLast(new StringDecoder());
            ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
              @Override
              protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                //获得实现类处理过后的返回值
                String invokeMethodMes = CommonDeal.getInvokeMethodMes(msg);
                ctx.writeAndFlush(Unpooled.wrappedBuffer(invokeMethodMes.getBytes()));
              }
            });
          }
        }).bind(10090);
  }
}
