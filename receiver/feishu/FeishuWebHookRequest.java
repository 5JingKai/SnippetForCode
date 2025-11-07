package org.example.receiver.feishu;

import lombok.Data;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

/**
 * @author junzhou.wu
 * @version 20241230
 **/
public @Data class FeishuWebHookRequest {
    private long timestamp;
    private String sign;
    private String msg_type;
    private PostContent content;

    public @Data
    static class PostContent {

        private PostContentPost post;

    }

    public @Data
    static class PostContentPost {
        private PostContentPostZhCn zh_cn;
    }

    public @Data
    static class PostContentPostZhCn {
        private String title;
        private List<List<PostContentPostZhCnMessage>> content;
    }

    /**
     * tag 取值：
     * - a, 对应字段：ext，href
     * - text， 对应字段： text；
     * - at， 对应字段：user_id, 如果是@某个人取值为用户id，如果发送给全部，使用 all 字符串。
     */
    public @Data
    static class PostContentPostZhCnMessage {
        private String tag;
        private String text;
        private String href;
        private String user_id;
        private String user_name;
    }


    public static class FeishuWebHookRequestBuilder {
        private FeishuWebHookRequest request;


        public FeishuWebHookRequestBuilder() {
            this.request = new FeishuWebHookRequest();
            this.request.setMsg_type("post");
            PostContent postContent = new PostContent();
            this.request.setContent(postContent);
            PostContentPost post = new PostContentPost();
            postContent.setPost(post);
            PostContentPostZhCn postContentPostZhCn = new PostContentPostZhCn();
            postContentPostZhCn.setContent(new ArrayList<>());
            post.setZh_cn(postContentPostZhCn);
        }

        /**
         * 富文本消息
         *
         * @return
         */
        public FeishuWebHookRequest build() {
            return this.request;
        }

        public FeishuWebHookRequestBuilder timstamp(long timestamp) {
            this.request.setTimestamp(timestamp);
            return this;
        }

        public FeishuWebHookRequestBuilder sign(String sign) {
            this.request.setSign(sign);
            return this;
        }

        @SneakyThrows
        public FeishuWebHookRequestBuilder signSmart(String secret, long timestamp) {
            String sign = FeishuWebHookSignHelper.genSign(secret, timestamp);
            this.request.setSign(sign);
            this.request.setTimestamp(timestamp);
            return this;
        }

        public FeishuWebHookRequestBuilder title(String title) {
            this.request.getContent().getPost().getZh_cn().setTitle(title);
            return this;
        }

        public FeishuWebHookRequestBuilder simpleMsgAtAll(String msg) {
            List<PostContentPostZhCnMessage> messages = new ArrayList<>();
            PostContentPostZhCnMessage message = new PostContentPostZhCnMessage();
            message.setTag("text");
            message.setText(msg);
            messages.add(message);

            List<PostContentPostZhCnMessage> atMessages = new ArrayList<>();

            PostContentPostZhCnMessage atMessage = new PostContentPostZhCnMessage();
            atMessage.setTag("at");
            atMessage.setUser_id("all");
            atMessage.setUser_name("所有人");
            atMessages.add(atMessage);

            this.request.getContent().getPost().getZh_cn().getContent().add(messages);
            this.request.getContent().getPost().getZh_cn().getContent().add(atMessages);
            return this;
        }

        public FeishuWebHookRequestBuilder simpleMsgWithoutAt(String msg) {
            List<PostContentPostZhCnMessage> messages = new ArrayList<>();
            PostContentPostZhCnMessage message = new PostContentPostZhCnMessage();
            message.setTag("text");
            message.setText(msg);
            messages.add(message);
            this.request.getContent().getPost().getZh_cn().getContent().add(messages);
            return this;
        }

    }
}
