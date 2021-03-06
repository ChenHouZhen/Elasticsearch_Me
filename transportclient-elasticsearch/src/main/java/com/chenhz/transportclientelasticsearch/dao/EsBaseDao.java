package com.chenhz.transportclientelasticsearch.dao;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.chenhz.transportclientelasticsearch.annotation.EsDocument;
import com.chenhz.transportclientelasticsearch.utils.EntityWrapper;
import com.chenhz.transportclientelasticsearch.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EsBaseDao<T> {

    @Autowired
    protected TransportClient client;


    public T selectById(String id){
        GetResponse response = client.prepareGet(this.getIndex(), this.getType(),
                id).get();
        return JSON.parseObject(response.getSourceAsString(),this.getEntityClass());
    }


    public List<T> listByIds(String... ids){
        SearchRequestBuilder searchRequestBuilder =this.getSearchRequestBuilder();
        EntityWrapper<T> wrapper = new EntityWrapper<>();
        wrapper.ids(ids);
        searchRequestBuilder.setQuery(wrapper.getBoolQueryBuilder());
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        log.info("查询："+searchRequestBuilder);
        log.info("条件："+wrapper.getBoolQueryBuilder());
        log.info("响应："+searchResponse);
        return this.data(searchResponse);
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


    protected SearchRequestBuilder getSearchRequestBuilder(){
        return client.prepareSearch(this.getEsDocument().index()).setTypes(this.getEsDocument().type());
    }


    public List<T> selectList(EntityWrapper<T> wrapper){
        SearchRequestBuilder searchRequestBuilder =this.getSearchRequestBuilder();
        searchRequestBuilder.setQuery(wrapper.getBoolQueryBuilder());
        if (wrapper.getSort()){
            searchRequestBuilder.addSort(wrapper.getSortBuilder());
        }
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        log.info("查询："+searchRequestBuilder);
        log.info("条件："+wrapper.getBoolQueryBuilder());
        log.info("响应："+searchResponse);
        return this.data(searchResponse);
    }

    public PageUtils selectPage(EntityWrapper<T> wrapper,Page<T> page){
        SearchRequestBuilder searchRequestBuilder =this.getSearchRequestBuilder();
        searchRequestBuilder.setQuery(wrapper.getBoolQueryBuilder());

        // 两个方案可以实现排序
        if (wrapper.getSort()){
            searchRequestBuilder.addSort(wrapper.getSortBuilder());
        }

     /*   if (page.isOpenSort() && page.isSearchCount()) {
            searchRequestBuilder.addSort(page.getOrderByField(),page.isAsc()? SortOrder.ASC : SortOrder.DESC);
        }*/
        searchRequestBuilder.setFrom((page.getCurrent() - 1) * page.getSize()).setSize(page.getSize());

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        log.info("查询："+searchRequestBuilder);
        log.info("条件："+wrapper.getBoolQueryBuilder());
        log.info("响应："+searchResponse);
        return this.dataPage(searchResponse,page);
    }

    public List<T> selectList(BoolQueryBuilder boolQueryBuilder){
        SearchRequestBuilder searchRequestBuilder =this.getSearchRequestBuilder();
        searchRequestBuilder.setQuery(boolQueryBuilder);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        log.info("查询："+searchRequestBuilder);
        log.info("条件："+boolQueryBuilder);
        log.info("响应："+searchResponse);
        return this.data(searchResponse);
    }

    private PageUtils dataPage(SearchResponse searchResponse, com.baomidou.mybatisplus.plugins.Page<T> page){
        List<T> data = this.data(searchResponse);
        long totalCount = searchResponse.getHits().getTotalHits();
        return new PageUtils(data,totalCount,page.getSize(),page.getCurrent());
    }


    private List<T> data(SearchResponse searchResponse){
        List<T> data = new ArrayList<>();
        for (SearchHit searchHit:searchResponse.getHits().getHits()) {
            T t = JSON.parseObject(searchHit.getSourceAsString(),getEntityClass());
            data.add(t);
        }
        return data;
    }


    public static <E> E getObject(List<E> list) {
        if (!CollectionUtils.isEmpty(list)) {
            int size = list.size();
            if (size > 1) {
                log.warn(String.format("Warn: execute Method There are  %s results.", size));
            }
            return list.get(0);
        }
        return null;
    }

}
