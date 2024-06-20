package com.lyc.pcelectricfence.enums;

/**
 * 应答结果类型枚举
 *
 * @author ChenQi
 * @date 2024/6/20
 */
public enum ResponseType {

    FAILURE(0, "失败"),
    SUCCESS(1, "成功"),
    EXECUTION_SUCCESS(2, "执行成功");

    private final int code;
    private final String description;

    ResponseType(int code, String description) {
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
