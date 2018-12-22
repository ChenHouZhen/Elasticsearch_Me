package com.chenhz.transportclientelasticsearch.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * @author CHZ
 * @create 2018/12/22
 */
@Component
@Getter
@Setter
@ToString
@ConfigurationProperties("app")
public class EsConfigProps {

    @NestedConfigurationProperty
    private Index index = new Index();

    @NestedConfigurationProperty
    private Es es = new Es();
}
