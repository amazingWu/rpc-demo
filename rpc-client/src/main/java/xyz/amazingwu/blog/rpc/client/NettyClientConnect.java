package xyz.amazingwu.blog.rpc.client;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import xyz.amazingwu.blog.rpc.common.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author amazingjadewu@gmail.com
 * @created 2020年02月28日 14:41
 */
@Slf4j
public class NettyClientConnect {

  private final static NioEventLoopGroup workGroup = new NioEventLoopGroup();

  private final static Map<Long, MessageFuture> futureMap = new ConcurrentHashMap<>();
  private CountDownLatch countDownLatch = new CountDownLatch(1);
  private Throwable throwable;

  public void connect(String requestJson, Long threadId) {
    throwable = null;
    Bootstrap bootstrap = new Bootstrap();
    ChannelFuture channelFuture = bootstrap.group(workGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<Channel>() {
      @Override
      protected void initChannel(Channel channel) {
        channel.pipeline()
            // 服务端返回消息string解码
            .addLast(new StringDecoder())
            // 服务端返回消息反序列化（此处为了简化起见，使用了JSON序列化）
            .addLast(new SimpleChannelInboundHandler<String>() {
              @Override
              protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
                Response response = JSON.parseObject(s, Response.class);
                MessageFuture messageFuture = futureMap.get(threadId);
                messageFuture.setMessage(response);
                countDownLatch.countDown();
              }
              @Override
              public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                futureMap.put(threadId, new MessageFuture());
              }
              @Override
              public void channelActive(ChannelHandlerContext ctx) {
                ctx.writeAndFlush(Unpooled.wrappedBuffer(requestJson.getBytes()));
              }
            });
      }
    }).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 2000)
        .connect("127.0.0.1", 10090);
    channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
      // 检查操作的状态
      if (channelFuture1.isSuccess()) {
        log.info("connect success");
      } else {
        // 如果发生错误，则访问描述原因的Throwable
        throwable = channelFuture1.cause();
        log.error("connect error", throwable);
        // 连接异常时释放锁
        countDownLatch.countDown();
      }
    });
  }

  public Response getResponse(Long threadId) throws Throwable {
    MessageFuture messageFuture = null;
    try {
      countDownLatch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    if (throwable != null) {
      throw throwable;
    }
    messageFuture = futureMap.get(threadId);
    System.out.println(JSON.toJSON(messageFuture));
    futureMap.remove(threadId);
    return messageFuture.getMessage();
  }
}
