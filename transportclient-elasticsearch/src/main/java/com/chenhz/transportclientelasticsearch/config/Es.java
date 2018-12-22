package com.chenhz.transportclientelasticsearch.config;

import lombok.Data;

/**
 * @author CHZ
 * @create 2018/12/22
 */
@Data
public class Es {
    private String cluster;
    private String ip;
    private Integer port;
    private Integer pool;
}
