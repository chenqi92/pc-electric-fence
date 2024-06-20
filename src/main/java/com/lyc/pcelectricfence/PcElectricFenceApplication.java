package com.lyc.pcelectricfence;

import cn.allbs.influx.annotation.EnableAllbsInflux;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAllbsInflux
@SpringBootApplication
public class PcElectricFenceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PcElectricFenceApplication.class, args);
    }

}
