package com.wangshjm.blog.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class EsConfig {
    @Value("${es.hosts}")
    private String esHosts;

    @Bean(name = "restClient")
    @Qualifier("restClient")
    public RestClient initRestClient() {

        RestClient restClient = RestClient.builder(covertHttpHosts())
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder;
                    }
                }).setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder.setConnectTimeout(5000).setSocketTimeout(60000);
                    }
                }).setMaxRetryTimeoutMillis(60000).build();
        return restClient;
    }

    @Bean
    public RestHighLevelClient initRestHighLevelClient(@Qualifier("restClient") RestClient restClient) {
        return new RestHighLevelClient(restClient);
    }

    // 集群配置转HttpHost数组
    private HttpHost[] covertHttpHosts() {
        String[] hosts = esHosts.split(",");
        List<HttpHost> httpHostList = new ArrayList<>();
        for (String host : hosts) {
            String[] sub = host.split(":");
            httpHostList.add(new HttpHost(sub[0], Integer.parseInt(sub[1])));
        }
        HttpHost[] array = new HttpHost[httpHostList.size()];
        return httpHostList.toArray(array);
    }
}
