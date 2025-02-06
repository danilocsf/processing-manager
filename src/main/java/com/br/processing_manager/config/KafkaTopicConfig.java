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

    @Value("${topic.activity.replicas:3}")
    private int activityReplicas;

    @Value("${topic.processing.replicas:3}")
    private int processingReplicas;

    @Value("${topic.processing.min.insync.replicas:1}")
    private String processingMinInSyncReplicas;

    @Value("${topic.activity.min.insync.replicas:1}")
    private String activityMinInSyncReplicas;

    private static final String MIN_INSYNC_REPLICAS = "min.insync.replicas";

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
                .configs(Map.of(MIN_INSYNC_REPLICAS, processingMinInSyncReplicas))
                .build();
    }

    @Bean
    public NewTopic activityTopicBuilder() {
        return TopicBuilder
                .name(activityTopic)
                .partitions(activityPartitions)
                .replicas(activityReplicas)
                .configs(Map.of(MIN_INSYNC_REPLICAS, activityMinInSyncReplicas))
                .build();
    }
}
