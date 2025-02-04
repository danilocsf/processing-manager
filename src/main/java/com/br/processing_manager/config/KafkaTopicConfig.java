package com.br.processing_manager.config;


import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaAddress;

    @Value("${topic.processing}")
    private String processingTopic;

    @Value("${topic.activity}")
    private String activityTopic;

    @Value("${topic.processing.partition:3}")
    private int processingPartitions;

    @Value("${topic.activity.partition:3}")
    private int activityPartitions;

    @Value("${topic.activity.replicas:1}")
    private int activityReplicas;

    @Value("${topic.processing.replicas:1}")
    private int processingReplicas;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic processingTopicBuilder() {
        return TopicBuilder
                .name(processingTopic)
                .partitions(processingPartitions)
                .replicas(processingReplicas)
                .build();
    }

    @Bean
    public NewTopic activityTopicBuilder() {
        return TopicBuilder
                .name(activityTopic)
                .partitions(activityPartitions)
                .replicas(activityReplicas)
                .build();
    }
}
