package org.example.receiver.feishu;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * @author junzhou.wu
 * @version 20241230
 **/
@Service
@Slf4j
public class FeishuWebHookSender {

    private @Autowired HttpClient httpClient;
    private @Autowired ObjectMapper objectMapper;

    @SneakyThrows
    public void send(String webHook, FeishuWebHookRequest requestBody) {

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .timeout(Duration.ofSeconds(10))
                    .uri(new URI(webHook))
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .header("habo", "habo")
                    .POST(HttpRequest.BodyPublishers.ofString(this.objectMapper.writeValueAsString(requestBody)))
                    .build();
            HttpResponse<InputStream> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                log.error("FeishuWebHookSender failed to send webhook, http response code: {}, message: {}", response.statusCode(), response.body());
            }
            PushbackInputStream in = new PushbackInputStream(response.body());
            FeishuWebHookResponse result = this.objectMapper.readValue(in, FeishuWebHookResponse.class);
            if (!result.isSuccess()) {
                log.error("FeishuWebHookSender failed to send webhook, resonse: {}", this.objectMapper.writeValueAsString(result));
            }
        } catch (Exception e) {
            log.error("调用飞书发送通知异常，请求内容：{} ", this.objectMapper.writeValueAsString(requestBody));
            log.error("调用飞书发送通知异常: ", e);
        }
    }
}
