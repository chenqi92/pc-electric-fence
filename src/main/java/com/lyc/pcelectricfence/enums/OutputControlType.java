package com.lyc.pcelectricfence.enums;

/**
 * 输出控制类型枚举
 *
 * @author ChenQi
 * @date 2024/6/20
 */
public enum OutputControlType {

    OFF(0, "断开"),
    ON(1, "闭合");

    private final int code;
    private final String description;

    OutputControlType(int code, String description) {
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
