package com.lyc.pcelectricfence.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 类 NettyServerHandlerInitializer
 *
 * @author ChenQi
 * @date 2024/6/21
 */
@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 3 * 60;

    @Resource
    private ProtocolHandler protocolHandler;

    @Override
    protected void initChannel(Channel ch) {
        // 获得 Channel 对应的 ChannelPipeline
        ChannelPipeline channelPipeline = ch.pipeline();
        ByteBuf delimiter = Unpooled.copiedBuffer("\r\n".getBytes());
        // @formatter:off
        channelPipeline
                // 空闲检测
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS))
                .addLast(new DelimiterBasedFrameDecoder(16 * 1024, false, delimiter))
                // 解码器
                .addLast(new StringDecoder())
                // 编码器
                .addLast(new StringEncoder())
                // 消息分发器
                .addLast(protocolHandler);
        // @formatter:on
    }

}
