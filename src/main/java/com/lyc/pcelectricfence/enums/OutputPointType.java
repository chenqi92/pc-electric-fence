package com.lyc.pcelectricfence.enums;

/**
 * 输出点类型枚举
 *
 * @author ChenQi
 * @date 2024/6/20
 */
public enum OutputPointType {

    OUTPUT(0, "输出"),
    LIGHT(1, "灯"),
    LED(2, "LED");

    private final int code;
    private final String description;

    OutputPointType(int code, String description) {
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
