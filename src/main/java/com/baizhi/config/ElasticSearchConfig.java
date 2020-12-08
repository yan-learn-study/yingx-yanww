package com.baizhi.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

/**
 * @author yww
 * @Description
 * @Date 2020/12/05 14:07
 */
@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient getRestHighLevelClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("16.20.0.4:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }


}
