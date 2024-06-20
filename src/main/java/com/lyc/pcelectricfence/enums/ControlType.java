package com.lyc.pcelectricfence.enums;

/**
 * 控制类型枚举
 *
 * @author ChenQi
 * @date 2024/6/20
 */
public enum ControlType {

    DISARM(0, "撤防"),
    ARM(1, "布防"),
    BYPASS(2, "旁路"),
    UNBYPASS(3, "解除旁路"),
    STAY_ARM(4, "留守布防"),
    HIGH_VOLTAGE_ARM(5, "高压布防"),
    LOW_VOLTAGE_ARM(6, "低压布防");

    private final int code;
    private final String description;

    ControlType(int code, String description) {
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
