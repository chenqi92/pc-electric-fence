package com.lyc.pcelectricfence.enums;

/**
 * 事件代码类型枚举
 *
 * @author ChenQi
 * @date 2024/6/20
 */
public enum EventType {

    ZONE_ARM(0, "防区布防事件"),
    ZONE_DISARM(1, "防区撤防事件"),
    ZONE_ALARM(2, "防区报警事件"),
    ZONE_ALARM_RECOVERY(3, "防区报警恢复事件"),
    DEVICE_TAMPER(4, "设备被撬事件"),
    DEVICE_TAMPER_RECOVERY(5, "设备被撬恢复事件"),
    DEVICE_LOW_VOLTAGE(6, "设备欠压事件"),
    DEVICE_LOW_VOLTAGE_RECOVERY(7, "设备欠压恢复事件"),
    DEVICE_CONNECTION_FAULT(8, "设备连接故障事件"),
    DEVICE_CONNECTION_RECOVERY(9, "设备连接恢复事件"),
    DEVICE_ARM(10, "设备布防"),
    DEVICE_DISARM(11, "设备撤防"),
    DEVICE_HIJACK(12, "设备挟持"),
    COMMUNICATION_FAULT(13, "通讯机连接故障事件"),
    COMMUNICATION_RECOVERY(14, "通讯机连接恢复事件"),
    ZONE_BYPASS(15, "防区旁路事件"),
    ZONE_BYPASS_RECOVERY(16, "防区旁路恢复事件"),
    DEVICE_EMERGENCY(17, "设备紧急事件"),
    DEVICE_EMERGENCY_RECOVERY(18, "设备紧急恢复事件"),
    DEVICE_FIRE(19, "设备火警事件"),
    DEVICE_FIRE_RECOVERY(20, "设备火警恢复事件"),
    ZONE_ARM_STATUS(21, "防区布防状态"),
    ZONE_DISARM_STATUS(22, "防区撤防状态"),
    ZONE_NOT_READY(23, "防区未准备"),
    ZONE_NOT_READY_RECOVERY(24, "防区未准备恢复"),
    STAY_ARM(25, "留守布防"),
    ZONE_FAULT(26, "防区故障"),
    ZONE_FAULT_RECOVERY(27, "防区故障恢复"),
    TELEPHONE_LINE_FAULT(28, "电话线故障"),
    TELEPHONE_LINE_RECOVERY(29, "电话线故障恢复"),
    BATTERY_FAULT(30, "电池故障"),
    BATTERY_RECOVERY(31, "电池恢复"),
    AC_FAULT(32, "交流故障"),
    AC_RECOVERY(33, "交流恢复"),
    FENCE_BREAK_ALARM(34, "围栏断线报警"),
    FENCE_SHORT_ALARM(35, "围栏短路报警"),
    FENCE_VOLTAGE(36, "围栏电压"),
    FENCE_TOUCH_ALARM(37, "触网报警");

    private final int code;
    private final String description;

    EventType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
