package com.lyc.pcelectricfence.constant;

/**
 * 接口 CommonConstant 常量定义
 *
 * @author ChenQi
 * @date 2024/6/21
 */
public interface CommonConstant {

    /**
     * 电子围栏数据表
     */
    String INFLUXDB_DATABASE_MEASUREMENT = "cl_electronic_patrol_alarm";

    /**
     * 时间格式
     */
    String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 报警信息中的时间格式
     */
    String ALARM_DATETIME_PATTERN = "yyyy年MM月dd日HH时mm分";
}
