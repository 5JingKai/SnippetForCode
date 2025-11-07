package org.example.receiver.feishu;

import lombok.Data;

import java.util.Map;

/**
 * @author junzhou.wu
 * @version 20241230
 **/
public @Data class FeishuWebHookResponse {
    private int code;
    private String msg;
    private Map<String, Object > data;

    public boolean isSuccess() {
        return code == 0;
    }

}
