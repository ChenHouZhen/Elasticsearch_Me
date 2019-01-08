package com.chenhz.transportclientelasticsearch.dao;

import com.alibaba.fastjson.JSON;
import com.chenhz.transportclientelasticsearch.annotation.EsDocument;
import com.chenhz.transportclientelasticsearch.utils.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EsBaseDao<T> {

    @Autowired
    private TransportClient client;


    public T selectById(String id){
        GetResponse response = client.prepareGet(this.getIndex(), this.getType(),
                id).get();
        return JSON.parseObject(response.getSourceAsString(),this.getEntityClass());
    }


    public T selectOne(EntityWrapper<T> wrapper) {
        return this.getObject(this.selectList(wrapper));
    }


    // 获取 T 的类型
    protected Class<T> getEntityClass(){
        return (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected String getIndex(){
        return this.getEsDocument().index();
    }


    protected String getType(){
        return this.getEsDocument().type();
    }

    private EsDocument getEsDocument(){
        EsDocument esDocument =  getEntityClass().getAnnotation(EsDocument.class);
        if (esDocument == null){
            throw new IllegalArgumentException("没有配置索引");
        }
        return esDocument;
    }


    protected SearchRequestBuilder initReflect(){
        return client.prepareSearch(this.getEsDocument().index()).setTypes(this.getEsDocument().type());
    }



    public List<T> selectList(EntityWrapper<T> wrapper){
        SearchRequestBuilder searchRequestBuilder =null;
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        return this.data(searchResponse, (Class<T>) wrapper.getEntity().getClass());
    }


    private List<T> data(SearchResponse searchResponse,Class<T> tClass){
        List<T> data = new ArrayList<>();
        for (SearchHit searchHit:searchResponse.getHits().getHits()) {
            T t = JSON.parseObject(searchHit.getSourceAsString(),tClass);
//            t.setId(searchHit.getId());
            data.add(t);
        }
        return data;
    }

    public static <E> E getObject(List<E> list) {
        if (CollectionUtils.isEmpty(list)) {
            int size = list.size();
            if (size > 1) {
                log.warn(String.format("Warn: execute Method There are  %s results.", size));
            }
            return list.get(0);
        }
        return null;
    }

}
