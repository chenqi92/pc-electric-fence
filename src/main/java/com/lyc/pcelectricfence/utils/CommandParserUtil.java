package com.lyc.pcelectricfence.utils;

import com.lyc.pcelectricfence.enums.*;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 类 CommandParserUtil
 *
 * @author ChenQi
 * @date 2024/6/20
 */
@Slf4j
public class CommandParserUtil {

    /**
     * 解析主机控制指令
     * C 主机编号 设备类型 防区编号 控制类型 密码
     * C 6130-1-128 0 0 123456    表示 控制6130号主机的128号键盘(即主键盘)撤防
     * C 6130-1-128 0 1 123456    表示 控制6130号主机的128号键盘(即主键盘)布防
     * C 6130-0-1 0 1 123456      表示 控制6130号主机的1号设备布防
     * C 6130-0-1 1 1 123456      表示 控制6130号主机的1号设备防区1布防
     * C 6130-0-1 0 0 123456      表示 控制6130号主机的1号设备撤防
     * C 6130-0-1 1 0 123456      表示 控制6130号主机的1号设备防区1撤防
     *
     * @param command 指令
     * @return 解析结果
     */
    public static Map<String, Object> parseHostControlCommand(String command) {
        Map<String, Object> map = new HashMap<>();
        String[] parts = command.split(" ");
        if (parts.length < 5) {
            log.error("Invalid command format: {}", command);
            return null;
        }
        map.put("host", parts[1]);
        // 主机编号
        String hostNumber = parts[1].split("-")[0];
        map.put("hostNumber", Integer.parseInt(hostNumber));
        int controlType = Integer.parseInt(parts[1].split("-")[1]);
        // 通讯机编号
        map.put("communicationNumber", controlType);
        String controlTypeName = controlType == 0 ? "设备" : "键盘";
        // 设备编号
        int deviceNumber = Integer.parseInt(parts[1].split("-")[2]);
        if (deviceNumber == 128) {
            controlTypeName = "键盘(即主键盘)";
        }
        map.put("deviceNumber", deviceNumber);
        // 设备类型
        int deviceType = Integer.parseInt(parts[2]);
        map.put("deviceType", deviceType);
        String deviceTypeName = deviceType == 0 ? "" : String.format("防区%d", deviceType);
        map.put("deviceTypeName", deviceType == 0 ? "设备" : "防区");
        // 控制类型
        int controlAction = Integer.parseInt(parts[3]);
        map.put("controlAction", controlAction);
        String controlActionTrans = ControlType.values()[controlAction].getDescription();
        map.put("controlActionTrans", controlActionTrans);
        // 密码
        String password = parts[4];
        map.put("password", password);

        String event = String.format("控制%s号主机的%s号%s%s%s", hostNumber, deviceNumber, controlTypeName, deviceTypeName, controlActionTrans);
        log.info("Host control event: {}", event);
        map.put("event", event);
        return map;
    }

    /**
     * 解析控制指令
     * O 主机编号 输出点类型 输出点号 控制类型 控制时间
     * O 6130-0-1 0 1 1 10    表示 控制6130号主机的1号设备的1号输出闭合10秒
     * O 6130-0-1 0 1 0 0      表示 控制6130号主机的1号设备的1号输出断开
     *
     * @param command 指令
     * @return 解析结果
     */
    public static Map<String, Object> parseOutputControlCommand(String command) {
        Map<String, Object> map = new HashMap<>();
        String[] parts = command.split(" ");
        if (parts.length < 6) {
            log.error("Invalid command format: {}", command);
            return null;
        }
        map.put("host", parts[1]);
        // 主机编号
        String hostNumber = parts[1].split("-")[0];
        map.put("hostNumber", Integer.parseInt(hostNumber));
        // 设备编号
        String deviceNumber = parts[1].split("-")[2];
        map.put("deviceNumber", Integer.parseInt(deviceNumber));
        // 输出点类型
        String zoneType = parts[2];
        map.put("zoneType", zoneType);
        // 输出点类型解析
        String zoneTypeName = OutputPointType.values()[Integer.parseInt(zoneType)].getDescription();
        map.put("zoneTypeName", zoneTypeName);
        // 输出点号
        int zoneNumber = Integer.parseInt(parts[3]);
        map.put("zoneNumber", zoneNumber);
        // 控制类型
        int controlAction = Integer.parseInt(parts[4]);
        map.put("controlAction", controlAction);
        String controlActionTrans = OutputControlType.values()[controlAction].getDescription();
        map.put("controlActionTrans", controlActionTrans);
        // 控制时间
        int controlTime = Integer.parseInt(parts[5]);
        map.put("controlTime", controlTime);
        String controlTrans = OutputControlType.OFF.getCode() == controlAction ? "" : controlTime + "秒";
        String event = String.format("控制%s号主机的%s号设备的%s号%s%s%s", hostNumber, deviceNumber, zoneNumber, zoneTypeName,
                controlActionTrans, controlTrans);
        log.info("Control event tans: {}", event);
        map.put("event", event);
        return map;
    }

    /**
     * 解析事件上传指令
     * E 主机编号 防区编号 事件类型 事件值
     * E 6130-0-1 4 2 0 11-7-13-2 表示6130主机1号模块4防区在11月7号13点2分
     * E 0001 1 36 50    表示1号主机的1号围栏 电压为5.0KV
     *
     * @param command 指令
     * @return 解析结果
     */
    public static Map<String, Object> parseEventUploadCommand(String command) {
        Map<String, Object> map = new HashMap<>();
        String[] parts = command.split(" ");
        map.put("host", parts[1]);
        if (parts.length == 5) {
            // E 0001 1 36 50
            int hostNumber = Integer.parseInt(parts[1]);
            map.put("hostNumber", hostNumber);

            int zoneNumber = Integer.parseInt(parts[2]);
            map.put("zoneNumber", zoneNumber);

            int eventType = Integer.parseInt(parts[3]);
            map.put("eventType", eventType);

            String eventTypeName = EventType.values()[eventType].getDescription();
            map.put("eventTypeName", eventTypeName);

            DecimalFormat df = new DecimalFormat("#.0"); // 保留一位小数
            float value = Float.parseFloat(parts[4]);
            map.put("value", df.format(value));
            String valueUnit = value + "";
            if (EventType.FENCE_VOLTAGE.getCode() == eventType) {
                map.put("value", df.format(value / 10));
                valueUnit = df.format(value / 10) + "KV";
            }

            String event = String.format("%s号主机的%s号%s为%s", hostNumber, zoneNumber, eventTypeName, valueUnit);
            log.info("Event upload: {}", event);
            map.put("event", event);

        } else if (parts.length == 6) {
            // E 6130-0-1 4 2 0 11-7-13-2
            String[] hostParts = parts[1].split("-");
            String hostNumber = hostParts[0];
            map.put("hostNumber", Integer.parseInt(hostNumber));
            // 通讯机编号
            int communicationNumber = Integer.parseInt(hostParts[1]);
            map.put("communicationNumber", communicationNumber);
            // 设备编号
            int deviceNumber = Integer.parseInt(hostParts[2]);
            map.put("deviceNumber", deviceNumber);
            DeviceEnum deviceEnum = DeviceEnum.fromCommAndTerminal(communicationNumber, deviceNumber);
            map.put("zoneType", communicationNumber + "-" + deviceNumber);
            map.put("zoneTypeName", deviceEnum.getDescription());

            // 防区
            int zoneNumber = Integer.parseInt(parts[2]);
            map.put("zoneNumber", zoneNumber);
            String zoneTypeName = zoneNumber > 0 ? zoneNumber + "防区" : "";

            int eventType = Integer.parseInt(parts[3]);
            map.put("eventType", eventType);

            String eventTypeName = EventType.values()[eventType].getDescription();
            map.put("eventTypeName", eventTypeName);

            // 子系统号固定为0
            String subsystemNumber = parts[4];
            map.put("subsystemNumber", Integer.parseInt(subsystemNumber));

            // 当前年份
            int year = LocalDate.now().getYear();
            String[] timeParts = parts[5].split("-");
            if (timeParts.length != 4) {
                log.error("Invalid time format: {}", command);
                return null;
            }
            LocalDateTime eventTime = LocalDateTime.of(year, Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]),
                    Integer.parseInt(timeParts[2]), Integer.parseInt(timeParts[3]));
            map.put("eventTime", eventTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            String event = String.format("%s号主机的%s号%s%s在%s发生%s", hostNumber, deviceNumber, deviceEnum.getOutput(), zoneTypeName, eventTime.format(DateTimeFormatter.ofPattern("MM月dd日HH时mm分")), eventTypeName);
            log.info("Event upload: {}", event);
            map.put("event", event);

        } else {
            log.error("Invalid command format: {}", command);
            return null;
        }

        return map;
    }
}
