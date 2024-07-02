package com.hydroyura.springboot.kafka.msg.wrapper.consumer.configs;

import com.hydroyura.springboot.kafka.msg.wrapper.consumer.services.MsgWrapperProcessor;
import com.hydroyura.springboot.kafka.msg.wrapper.consumer.services.MsgWrapperConsumer;
import com.hydroyura.springboot.kafka.msg.wrapper.consumer.services.processors.DefaultProcessor;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;


@AutoConfiguration
@Import(value = ConsumerProperties.class)
public class ConsumerConfig {

    @Autowired
    private ConsumerProperties consumerProperties;

    @Bean
    ConsumerFactory<String, String> consumerFactory() {
        var props = new HashMap<String, Object>();
        props.put(KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        props.put(MAX_POLL_RECORDS_CONFIG, 3);
        props.put(MAX_POLL_INTERVAL_MS_CONFIG, 3_000);
        props.put(BOOTSTRAP_SERVERS_CONFIG, List.of(consumerProperties.getUrl()));

        var consumerFactory = new DefaultKafkaConsumerFactory<String, String>(props);
        return consumerFactory;
    }

    @Bean
    KafkaListenerContainerFactory<?> listenerContainerFactory(ConsumerFactory<String, String> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(1);
        factory.setBatchListener(false);
        factory.setRecordMessageConverter(new StringJsonMessageConverter());
        return factory;
    }

    @Bean
    MsgWrapperConsumer msgWrapperConsumer(@Autowired(required = false) Collection<MsgWrapperProcessor<?>> processors) {
        processors = (processors == null) ? Collections.emptyList() : processors;
        return new MsgWrapperConsumer(processors);
    }

    @Bean
    DefaultProcessor defaultProcessor() {
        return new DefaultProcessor();
    }

}
