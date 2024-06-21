package com.lyc.pcelectricfence.netty;

import com.lyc.pcelectricfence.constant.CommonConstant;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                // 添加连接事件处理
                .addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern(CommonConstant.NORM_DATETIME_PATTERN)) + " Client connected: " + ctx.channel().remoteAddress());
                        super.channelActive(ctx);
                    }
                })
                // 消息分发器
                .addLast(protocolHandler);
        // @formatter:on
    }

}
