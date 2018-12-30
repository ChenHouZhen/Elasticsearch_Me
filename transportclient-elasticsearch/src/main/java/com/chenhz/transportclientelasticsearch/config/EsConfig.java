package com.chenhz.transportclientelasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author CHZ
 * @create 2018/12/22
 */
@Slf4j
@Configuration
public class EsConfig {

    @Autowired
    EsConfigProps esConfigProps;

    @Bean
    public TransportClient init(){
        TransportClient transportClient = null;
        try {
            Settings esSetting = Settings.builder()
                    .put("cluster.name",esConfigProps.getEs().getCluster())
                //    .put("client.transport.sniff",true)//增加嗅探机制，找到ES集群
//                    .put("thread_pool.search.size",Integer.parseInt(poolSize))//增加线程池个数，暂时设为5 推荐算法：int（（ 核心数 ＊ 3 ）／ 2 ）＋ 1
                    .build();

            transportClient = new PreBuiltTransportClient(esSetting);
            TransportAddress transportAddress = new TransportAddress(
                    InetAddress.getByName(esConfigProps.getEs().getIp()),esConfigProps.getEs().getPort());
            transportClient.addTransportAddresses(transportAddress);
        } catch (UnknownHostException e) {
            log.error("Elasticsearch TransportClient create ERROR !",e);
        }

        return transportClient;
    }
}
