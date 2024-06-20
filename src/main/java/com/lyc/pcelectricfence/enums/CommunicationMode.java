package com.lyc.pcelectricfence.enums;

/**
 * 通讯方式类型枚举
 * 本程序使用tcp接收，没有串口实施环境，就不写串口的实现了，所以本枚举没有用到
 *
 * @author ChenQi
 * @date 2024/6/20
 */
public enum CommunicationMode {

    SERIAL(0, "串口/网络"),
    GPRS(2, "GPRS");

    private final int code;
    private final String description;

    CommunicationMode(int code, String description) {
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
