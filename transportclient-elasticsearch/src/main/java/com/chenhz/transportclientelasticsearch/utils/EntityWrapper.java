package com.chenhz.transportclientelasticsearch.utils;

import com.chenhz.transportclientelasticsearch.annotation.EsDocument;
import com.chenhz.transportclientelasticsearch.entity.Doc;
import com.chenhz.transportclientelasticsearch.entity.User;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.io.Serializable;

public class EntityWrapper<T> implements Serializable {

    /**
     * ES 映射实体类
     */
    protected T entity = null;

   // protected TransportClient client;

    //protected SearchRequestBuilder searchRequestBuilder;

    protected BoolQueryBuilder boolQueryBuilder;

//    public SearchRequestBuilder getSearchRequestBuilder() {
//        return searchRequestBuilder;
//    }

    public EntityWrapper() {
        /* 注意，传入查询参数 */
    }

    public EntityWrapper(T entity) {
        this.entity = entity;
        init();
    }

    private void init(){
        // 初始化 客户端
       // this.client = SpringContextUtil.getBean(TransportClient.class);
        // 反射获取 索引
      // Doc d = initReflect();
        // 初始化 查询构造器
        //this.searchRequestBuilder = client.prepareSearch(d.getName()).setTypes(d.getType());

        // 初始化复杂查询构造器
        this.boolQueryBuilder = QueryBuilders.boolQuery();
    }
//
//    private Doc initReflect(){
//        EsDocument esDocument =  this.entity.getClass().getAnnotation(EsDocument.class);
//        if (esDocument == null){
//            throw new IllegalArgumentException("没有配置索引");
//        }
//        return new Doc(esDocument.index(),esDocument.type());
//    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public EntityWrapper<T> eq(String column, Object params) {
        TermQueryBuilder whereCql =  QueryBuilders.termQuery(column,params);
        this.boolQueryBuilder.must(whereCql);
        return this;
    }


    public EntityWrapper<T> or(String sqlOr, Object... params) {

        return this;
    }

    public EntityWrapper<T> orNew(String sqlOr, Object... params) {

        return this;
    }

    public EntityWrapper<T> and(String sqlAnd, Object... params) {

        return this;
    }
    public EntityWrapper<T> andNew(String sqlAnd, Object... params) {

        return this;
    }

    public EntityWrapper<T> groupBy(String columns) {

        return this;
    }

    public EntityWrapper<T> orderBy(String columns) {

        return this;
    }

    public EntityWrapper<T> like(String column, String value) {

        return this;
    }

    public EntityWrapper<T> in(String column, String value) {

        return this;
    }

}
