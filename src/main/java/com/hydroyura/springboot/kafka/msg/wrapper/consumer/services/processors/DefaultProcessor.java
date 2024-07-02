package com.hydroyura.springboot.kafka.msg.wrapper.consumer.services.processors;

import com.hydroyura.springboot.kafka.msg.wrapper.consumer.services.MsgWrapperProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultProcessor implements MsgWrapperProcessor<Object> {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Override
    public void processInternal(Object msg) {
        LOG.warn("Default processor is handing msg ....");
        LOG.warn("Msg type = [{}], value toString = [{}]", msg.getClass(), msg);
    }

    @Override
    public Class<Object> getEventClass() {
        return Object.class;
    }

    @Override
    public String getEventType() {
        return "";
    }
}
