package org.example.receiver.feishu;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author junzhou.wu
 * @version 20241230
 **/
@ConfigurationProperties(prefix = "feishu")
@Component
public @Data class FeishuProperties {

    private String profitRefresherWebHook;

    private String profitRefresherWebHookSecret;

    private String interfaceMonitorWebHook;

    private String interfaceMonitorWebHookSecret;

}
