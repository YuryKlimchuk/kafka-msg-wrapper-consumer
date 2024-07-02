package com.hydroyura.springboot.kafka.msg.wrapper.consumer.services;

import java.util.AbstractMap;
import java.util.Map;

public interface MsgWrapperProcessor<T> {

    default void process(Object event) {
        processInternal(getEventClass().cast(event));
    }

    default Map.Entry<String, Class<?>> getKey() {
        return new AbstractMap.SimpleEntry<>(getEventType(), getEventClass());
    }

    void processInternal(T event);
    Class<T> getEventClass();
    String getEventType();
}
