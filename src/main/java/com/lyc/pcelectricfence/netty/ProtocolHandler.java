package com.lyc.pcelectricfence.netty;

import com.lyc.pcelectricfence.enums.CommunicationMode;
import com.lyc.pcelectricfence.enums.DeviceType;
import com.lyc.pcelectricfence.enums.ResponseType;
import com.lyc.pcelectricfence.utils.CommandParserUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 类 ProtocolHandler
 *
 * @author ChenQi
 * @date 2024/6/20
 */
@Slf4j
public class ProtocolHandler extends SimpleChannelInboundHandler<String> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("Received message: {}", msg);
        String[] parts = msg.trim().split(" ");
        switch (parts[0]) {
            case "A":
                handleResponse(ctx, parts);
                break;
            case "H":
                handleHeartbeat(ctx, parts);
                break;
            case "E":
                handleEventUpload(ctx, parts);
                break;
            case "C":
                handleControl(ctx, parts);
                break;
            case "O":
                handleOutputControl(ctx, parts);
                break;
            default:
                // Handle unknown command
                break;
        }
    }

    /**
     * 处理响应
     *
     * @param ctx   ChannelHandlerContext
     * @param parts 消息分割后的数组
     */
    private void handleResponse(ChannelHandlerContext ctx, String[] parts) {
        int result = Integer.parseInt(parts[1]);
        ResponseType responseType = ResponseType.values()[result];
        log.info("Response: {}", responseType.getDescription());
        ctx.writeAndFlush("A 1\r\n");
    }

    /**
     * 处理心跳
     *
     * @param ctx   ChannelHandlerContext
     * @param parts 消息分割后的数组
     */
    private void handleHeartbeat(ChannelHandlerContext ctx, String[] parts) {
        int deviceNumber = Integer.parseInt(parts[1]);
        CommunicationMode communicationMode = CommunicationMode.values()[Integer.parseInt(parts[2])];
        DeviceType deviceType = DeviceType.values()[Integer.parseInt(parts[3])];
        log.info("Heartbeat - Device Number: {}, Communication Mode: {}, Device Type: {}", deviceNumber, communicationMode.getDescription(), deviceType.getDescription());
    }

    /**
     * 处理事件上传
     *
     * @param ctx   ChannelHandlerContext
     * @param parts 消息分割后的数组
     */
    private void handleEventUpload(ChannelHandlerContext ctx, String[] parts) {
        // 事件上传，储存至influxDb
        String command = String.join(" ", parts);
        Map<String, Object> map = CommandParserUtil.parseEventUploadCommand(command);
        log.info("Event Upload Command: {}", map);
    }

    /**
     * 处理控制
     *
     * @param ctx   ChannelHandlerContext
     * @param parts 消息分割后的数组
     */
    private void handleControl(ChannelHandlerContext ctx, String[] parts) {
        // 处理控制 TODO
        String command = String.join(" ", parts);
        Map<String, Object> map = CommandParserUtil.parseHostControlCommand(command);
        log.info("Control Command: {}", map);
    }

    /**
     * 处理输出控制
     *
     * @param ctx   ChannelHandlerContext
     * @param parts 消息分割后的数组
     */
    private void handleOutputControl(ChannelHandlerContext ctx, String[] parts) {
        // 输出控制 TODO
        String command = String.join(" ", parts);
        Map<String, Object> map = CommandParserUtil.parseOutputControlCommand(command);
        log.info("Output Control Command: {}", map);
    }
}
