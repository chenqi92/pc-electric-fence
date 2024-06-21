package com.lyc.pcelectricfence.enums;

/**
 * 枚举 DeviceEnum
 *
 * @author ChenQi
 * @date 2024/6/20
 */
public enum DeviceEnum {

    // 通讯机编号为0时的终端设备
    COMM_0_MAINBOARD(0, "0", "主机主板防区", "主板"),
    COMM_0_EXTENDED_1_64(0, "1-64", "扩展设备", "模块"),

    // 通讯机编号为1时的终端设备
    COMM_1_MAIN_KEYBOARD(1, "128", "主键盘", "键盘(即主键盘)"),
    COMM_1_SUB_KEYBOARD_129_160(1, "129-160", "分键盘", "键盘");

    private final int commNumber;
    private final String terminalRange;
    private final String description;
    private final String output;

    DeviceEnum(int commNumber, String terminalRange, String description, String output) {
        this.commNumber = commNumber;
        this.terminalRange = terminalRange;
        this.description = description;
        this.output = output;
    }

    public int getCommNumber() {
        return commNumber;
    }

    public String getTerminalRange() {
        return terminalRange;
    }

    public String getDescription() {
        return description;
    }

    public String getOutput() {
        return output;
    }

    public static DeviceEnum fromCommAndTerminal(int commNumber, int terminalNumber) {
        for (DeviceEnum device : DeviceEnum.values()) {
            if (device.getCommNumber() == commNumber) {
                String[] rangeParts = device.getTerminalRange().split("-");
                int rangeStart = Integer.parseInt(rangeParts[0]);
                int rangeEnd = rangeParts.length > 1 ? Integer.parseInt(rangeParts[1]) : rangeStart;
                if (terminalNumber >= rangeStart && terminalNumber <= rangeEnd) {
                    return device;
                }
            }
        }
        throw new IllegalArgumentException("Unknown device for commNumber: " + commNumber + ", terminalNumber: " + terminalNumber);
    }
}
