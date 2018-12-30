package com.chenhz.transportclientelasticsearch.config;

import com.chenhz.transportclientelasticsearch.utils.Generator;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;
import java.util.Random;

/**
 * @author CHZ
 * @create 2018/12/31
 */
@Configuration
public class BeanConfig {

    @Bean
    public Faker faker(){
        return new Faker(new Locale("zh_CN"));
    }

    @Bean
    public Random random(){
        return new Random();
    }

    @Bean
    public Generator generator(){
        return new Generator();
    }
}
