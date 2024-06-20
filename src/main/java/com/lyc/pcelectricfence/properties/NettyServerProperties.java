package com.lyc.pcelectricfence.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 作为netty服务端的配置类
 *
 * @author ChenQi
 * @date 2024/6/20
 */
@Data
@Component
@ConfigurationProperties(prefix = "netty.server")
public class NettyServerProperties {

    /**
     * 端口
     */
    private Integer port;
}
