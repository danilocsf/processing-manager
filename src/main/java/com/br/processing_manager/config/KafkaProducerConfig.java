package com.br.processing_manager.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Autowired
    private KafkaProperties kafkaProperties;

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaAddress;

    @Value("${batch.size}")
    private int batchSize;

    @Value("${linger.ms}")
    private int lingerMs;

    @Value("${spring.kafka.producer.transaction-id-prefix}")
    private String transactionalIdPrefix;

    @Value("${producer.ack}")
    private String ack;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> properties = kafkaProperties.buildProducerProperties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, transactionalIdPrefix);
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        properties.put(ProducerConfig.ACKS_CONFIG, ack);
        return new DefaultKafkaProducerFactory(properties);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public KafkaTransactionManager<String, String> kafkaTransactionManager() {
        return new KafkaTransactionManager<>(producerFactory());
    }

}