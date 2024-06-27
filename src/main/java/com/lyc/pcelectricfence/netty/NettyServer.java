package com.lyc.pcelectricfence.netty;

import com.lyc.pcelectricfence.properties.NettyServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 类 NettyServer
 *
 * @author ChenQi
 * @date 2024/6/20
 */
@Slf4j
@Component
public class NettyServer {

    @Resource
    private NettyServerProperties nettyServerProperties;

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 3 * 60;

    /**
     * boss 线程组，用于服务端接受客户端的连接
     */
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    /**
     * worker 线程组，用于服务端接受客户端的数据读写
     */
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Resource
    private ProtocolHandler protocolHandler;

    /**
     * Netty Server Channel
     */
    private Channel channel;

    /**
     * 启动 Netty Server
     */
    @PostConstruct
    public void start() throws InterruptedException {
        // 创建 ServerBootstrap 对象，用于 Netty Server 启动
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 作为分隔符的数据包,防止粘包，虽然报文说是回车分隔，实际上没有，所以未使用
//        ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes());
        serverBootstrap.group(bossGroup, workerGroup)
                // 指定 Channel 为服务端 NioServerSocketChannel
                .channel(NioServerSocketChannel.class)
                // 端口
                .localAddress(new InetSocketAddress(nettyServerProperties.getPort()))
                // 服务端接收队列的大小
                .option(ChannelOption.SO_BACKLOG, 1024)
                // TCP Keepalive 机制，实现 TCP 层级的心跳保活功能
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 允许较小的数据包的发送，降低延迟
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 空闲检测
                        ch.pipeline().addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS));
//                        ch.pipeline().addLast(new DelimiterBasedFrameDecoder(16 * 1024, false, delimiter));
                        // 解码器，因为客户端发送的是字符串所以直接用String即可，如果是其他的需要对应的解码
                        ch.pipeline().addLast(new StringDecoder());
                        // 跟编码器同理
                        ch.pipeline().addLast(new StringEncoder());
                        // 处理收到报文的方法
                        ch.pipeline().addLast(protocolHandler);
                    }
                });

        // 绑定端口，并同步等待成功，即启动服务端
        ChannelFuture future = serverBootstrap.bind().sync();
        if (future.isSuccess()) {
            channel = future.channel();
            log.info("netty服务端已启动,启动端口为{}", nettyServerProperties.getPort());
        }
    }

    @PreDestroy
    public void stop() {
        // 关闭 Netty Server
        if (channel != null) {
            channel.close();
        }
        // 优雅关闭两个 EventLoopGroup 对象
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
