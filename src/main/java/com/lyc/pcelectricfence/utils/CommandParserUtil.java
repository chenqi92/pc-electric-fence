package com.lyc.pcelectricfence.utils;

import com.lyc.pcelectricfence.enums.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 类 CommandParserUtil
 *
 * @author ChenQi
 * @date 2024/6/20
 */
public class CommandParserUtil {

    public static String parseControlCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 5) {
            return "无效的指令格式";
        }

        String hostNumber = parts[0];
        String zoneNumber = parts[1];
        ControlType controlType = ControlType.values()[Integer.parseInt(parts[3])];
        String password = parts[4];

        String deviceTypeDescription = getDeviceTypeDescription(zoneNumber);
        String controlTypeDescription = controlType.getDescription();

        return String.format("控制%s号主机的%s%s", hostNumber, deviceTypeDescription, controlTypeDescription);
    }

    public static String parseOutputControlCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 6) {
            return "无效的指令格式";
        }

        String hostNumber = parts[0];
        String zoneNumber = parts[1];
        OutputPointType outputPointType = OutputPointType.values()[Integer.parseInt(parts[2])];
        int outputPointNumber = Integer.parseInt(parts[3]);
        OutputControlType outputControlType = OutputControlType.values()[Integer.parseInt(parts[4])];
        int controlTime = Integer.parseInt(parts[5]);

        String deviceTypeDescription = getDeviceTypeDescription(zoneNumber);
        String outputPointTypeDescription = outputPointType.getDescription();
        String outputControlTypeDescription = outputControlType.getDescription();

        return String.format("控制%s号主机的%s的%s%d号输出点%s%d秒", hostNumber, deviceTypeDescription, outputPointTypeDescription, outputPointNumber, outputControlTypeDescription, controlTime);
    }

    public static String parseEventUploadCommand(String command) {
        String[] parts = command.split(" ");
        if (parts.length < 7) {
            return "无效的指令格式";
        }

        String hostNumber = parts[0];
        String zoneNumber = parts[1];
        EventType eventType = EventType.values()[Integer.parseInt(parts[2])];
        String subsystemNumber = parts[3];
        String eventTime = parts[4] + "-" + parts[5] + "-" + parts[6] + "-" + parts[7];

        String eventTypeDescription = eventType.getDescription();

        return String.format("主机编号: %s, 防区编号: %s, 事件类型: %s, 子系统号: %s, 事件时间: %s",
                hostNumber, zoneNumber, eventTypeDescription, subsystemNumber, eventTime);
    }

    public static Map<String, String> parseCommandToMap(String command) {
        String[] parts = command.split(" ");
        Map<String, String> commandMap = new HashMap<>();

        switch (parts[0]) {
            case "A":
                commandMap.put("类型", "应答");
                commandMap.put("结果", ResponseType.values()[Integer.parseInt(parts[1])].getDescription());
                break;
            case "H":
                commandMap.put("类型", "心跳");
                commandMap.put("设备编号", parts[1]);
                commandMap.put("通讯方式", CommunicationMode.values()[Integer.parseInt(parts[2])].getDescription());
                commandMap.put("设备类型", DeviceType.values()[Integer.parseInt(parts[3])].getDescription());
                break;
            case "E":
                commandMap.put("类型", "事件上传");
                commandMap.put("主机编号", parts[1]);
                commandMap.put("防区编号", parts[2]);
                commandMap.put("事件类型", EventType.values()[Integer.parseInt(parts[3])].getDescription());
                commandMap.put("子系统号", parts[4]);
                commandMap.put("时间", parts[5] + "-" + parts[6] + "-" + parts[7] + "-" + parts[8]);
                break;
            case "C":
                commandMap.put("类型", "主机控制");
                commandMap.put("主机编号", parts[1]);
                commandMap.put("防区编号", parts[2]);
                commandMap.put("控制类型", ControlType.values()[Integer.parseInt(parts[3])].getDescription());
                commandMap.put("密码", parts[4]);
                break;
            case "O":
                commandMap.put("类型", "输出点控制");
                commandMap.put("主机编号", parts[1]);
                commandMap.put("输出点类型", OutputPointType.values()[Integer.parseInt(parts[2])].getDescription());
                commandMap.put("输出点号", parts[3]);
                commandMap.put("控制类型", OutputControlType.values()[Integer.parseInt(parts[4])].getDescription());
                commandMap.put("控制时间", parts[5]);
                break;
            default:
                commandMap.put("类型", "未知");
                break;
        }

        return commandMap;
    }

    private static String getDeviceTypeDescription(String zoneNumber) {
        String[] zoneParts = zoneNumber.split("-");
        if (zoneParts.length < 2) {
            return "未知设备";
        }

        int commNumber = Integer.parseInt(zoneParts[1]);
        int terminalNumber = Integer.parseInt(zoneParts[2]);

        if (commNumber == 0) {
            if (terminalNumber == 0) {
                return "主机主板防区";
            } else if (terminalNumber <= 64) {
                return String.format("%d号设备", terminalNumber);
            }
        } else if (commNumber == 1) {
            if (terminalNumber == 128) {
                return "128号键盘(即主键盘)";
            } else if (terminalNumber > 128 && terminalNumber <= 160) {
                return String.format("%d号分键盘", terminalNumber - 128);
            }
        }

        return "未知设备";
    }
}
