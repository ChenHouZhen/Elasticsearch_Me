package com.chenhz.transportclientelasticsearch.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * @author CHZ
 * @create 2018/12/22
 */
@Data
@Component
@ConfigurationProperties("app.index")
public class IndexConfigProps {

    @NestedConfigurationProperty
    private IndexDoc indexDoc = new IndexDoc();


    @NestedConfigurationProperty
    private IndexTime indexTime = new IndexTime();
}
