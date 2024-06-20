package com.lyc.pcelectricfence.enums;

/**
 * 设备类型枚举
 *
 * @author ChenQi
 * @date 2024/6/20
 */
public enum DeviceType {

    ALARM_RECEIVER(0, "接警机"),
    HOST(1, "主机");

    private final int code;
    private final String description;

    DeviceType(int code, String description) {
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
