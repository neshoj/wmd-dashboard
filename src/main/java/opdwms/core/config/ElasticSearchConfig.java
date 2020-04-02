package opdwms.core.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class ElasticSearchConfig  {

    @Autowired
    private Environment environment;

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(environment.getProperty("wms.elasticsearch.ip"), Integer.valueOf(environment.getProperty("wms.elasticsearch.port"))))
                .setRequestConfigCallback(
                        (requestConfigBuilder) -> (
                                requestConfigBuilder
                                        .setConnectTimeout(Integer.valueOf(environment.getProperty("wms.elasticsearch.connectiontimeout")))
                                        .setSocketTimeout(Integer.valueOf(environment.getProperty("wms.elasticsearch.sockettimeout")))

                        ));
        return new RestHighLevelClient(builder);
    }

}
