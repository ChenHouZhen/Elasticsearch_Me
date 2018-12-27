package com.chenhz.transportclientelasticsearch.utils;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author CHZ
 * @create 2018/12/23
 */
@Component
@Slf4j
public class EsIndexUtils {


    @Autowired
    private TransportClient client;

    /**
     * 创建索引
     */
    public boolean createIndex(String index){
        if (isExistIndex(index)){
            log.warn("create index false ... Index >>> [{}] is already exists ! " , index);
            return true;
        }
        CreateIndexResponse indexResponse = client
                .admin()  //admin()，返回一个可以执行管理性操作的客户端；
                .indices()
                .prepareCreate(index)
                .execute()
                .actionGet();
        boolean result = indexResponse.isAcknowledged();
        log.info("create Index >>> [{}] successfully ?", result);
        return result;
    }
    /**
     * 判断索引是否存在
     * true :存在 false: 不存在
     */
    public boolean isExistIndex(String index){
        IndicesExistsResponse indicesExistsResponse = client
                .admin()
                .indices()
                .exists(new IndicesExistsRequest(index))
                .actionGet();
        boolean isExist = indicesExistsResponse.isExists();
        log.info("Index >>> [{}] is exist ? :[{}]" , index , isExist);
        return isExist;
    }

    /**
     * 删除索引
     */
    public boolean deleteIndex(String index){
        if (!isExistIndex(index)){
            log.warn("delete index false, because index >>> [{}] is not exist!",index);
            return true;
        }

        DeleteIndexResponse deleteIndexResponse= client
                .admin()
                .indices()
                .prepareDelete(index)
                .execute()
                .actionGet();
        boolean result  = deleteIndexResponse.isAcknowledged();
        log.info("delete Index >>> [{}] successfully ?",result);
        return result;
    }
}
