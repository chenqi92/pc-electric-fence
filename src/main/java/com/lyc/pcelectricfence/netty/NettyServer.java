package com.lyc.pcelectricfence.netty;

import com.lyc.pcelectricfence.properties.NettyServerProperties;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
     * Netty Server Channel
     */
    private Channel channel;

    /**
     * 启动 Netty Server
     */
    @PostConstruct
    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ProtocolHandler());
                        }
                    });

            ChannelFuture future = b.bind(nettyServerProperties.getPort()).sync().channel().closeFuture().sync();
            if (future.isSuccess()) {
                channel = future.channel();
                log.info("netty服务端已启动,启动端口为{}", nettyServerProperties.getPort());
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
