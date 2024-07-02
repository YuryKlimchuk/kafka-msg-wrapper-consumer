package com.hydroyura.springboot.kafka.msg.wrapper.consumer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hydroyura.springboot.kafka.msg.wrapper.consumer.services.processors.DefaultProcessor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.JacksonUtils;

import java.util.*;

public class MsgWrapperConsumer {

    private Logger LOG = LoggerFactory.getLogger(this.getClass());
    private ObjectMapper objectMapper = JacksonUtils.enhancedObjectMapper();


    private final Map<Map.Entry<String, Class<?>>, MsgWrapperProcessor<?>> eventProcessors = new HashMap<>();
    private final MsgWrapperProcessor<?> defaultProcessor;


    public MsgWrapperConsumer(Collection<MsgWrapperProcessor<?>> processors) {
        defaultProcessor = processors
                .stream()
                .filter(p -> p.getClass().equals(DefaultProcessor.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Missing default processor"));
        processors
                .stream()
                .filter(p -> !p.getClass().equals(DefaultProcessor.class))
                .forEach(p -> eventProcessors.put(p.getKey(), p));
    }

    @KafkaListener(topics = "#{'${kafka-msg-wrapper.consumer.topic}'}", groupId = "#{'${kafka-msg-wrapper.consumer.groupId}'}")
    void receive(ConsumerRecord<String, String> msg) {
        Map<String, Object> resultMap = Collections.emptyMap();
        try {
            resultMap = objectMapper.readValue(msg.value(), new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException ex) {
            LOG.error("Cannot convert msg with type [String] into msg with type [Map<String, Object>]");
            return;
        }

        Optional<String> type = Optional
                .ofNullable(resultMap.get("type"))
                .map(String.class::cast);

        Optional<Class<?>> clazz = Optional
                .ofNullable(resultMap.get("className"))
                .map(String.class::cast)
                .map(MsgWrapperConsumer::forName);

        Optional<Object> event = Optional
                .ofNullable(resultMap.get("body"));

        if (type.isPresent() && clazz.isPresent() && event.isPresent()) {
            Map.Entry<String, Class<?>> key = new AbstractMap.SimpleEntry<>(type.get(), clazz.get());
            MsgWrapperProcessor<?> eventProcessor = eventProcessors.getOrDefault(key, defaultProcessor);
            eventProcessor.process(objectMapper.convertValue(event.get(), eventProcessor.getEventClass()));
        } else {
           LOG.error("Cannot parse msg to MsgContainer format: mandatory fields -> type, body, className");
        }
    }

    private static Class<?> forName(String s) {
        try {
            return Class.forName(s);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
