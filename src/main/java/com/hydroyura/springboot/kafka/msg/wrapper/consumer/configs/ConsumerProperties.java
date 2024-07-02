package com.hydroyura.springboot.kafka.msg.wrapper.consumer.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "kafka-msg-wrapper.consumer")
public class ConsumerProperties {

    private String url, topic, groupId;


    public ConsumerProperties() {
        initDefaultValues();
    }


    private void initDefaultValues() {
        this.url = "localhost:9092";
        this.topic = "DEFAULT_TOPIC";
        this.groupId = "DEFAULT_GROUP_ID";
    }

    public String getUrl() {
        return url;
    }

    public ConsumerProperties setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public ConsumerProperties setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getGroupId() {
        return groupId;
    }

    public ConsumerProperties setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }
}
