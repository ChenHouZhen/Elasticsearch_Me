package com.chenhz.transportclientelasticsearch.config;

import com.chenhz.transportclientelasticsearch.entity.Doc;
import lombok.Data;
import lombok.ToString;
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
@ToString
public class IndexConfigProps {

    @NestedConfigurationProperty
    private Doc doc = new Doc();

}
