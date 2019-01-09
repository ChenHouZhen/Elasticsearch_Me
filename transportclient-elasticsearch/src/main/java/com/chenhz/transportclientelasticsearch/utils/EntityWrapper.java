package com.chenhz.transportclientelasticsearch.utils;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.sort.SortOrder;

import java.io.Serializable;
import java.util.Collection;

@Slf4j
public class EntityWrapper<T> implements Serializable {

    /**
     * ES 映射实体类
     */
   // protected T entity = null;

    protected BoolQueryBuilder boolQueryBuilder;


    private SearchRequestBuilder searchRequestBuilder;


    public BoolQueryBuilder getBoolQueryBuilder() {
        return boolQueryBuilder;
    }

    public SearchRequestBuilder getSearchRequestBuilder() {
        return searchRequestBuilder;
    }

    public void setSearchRequestBuilder(SearchRequestBuilder searchRequestBuilder) {
        this.searchRequestBuilder = searchRequestBuilder;
    }

    public EntityWrapper() {
        init();
        /* 注意，传入查询参数 */
    }


    private void init(){
        // 初始化复杂查询构造器
        this.boolQueryBuilder = QueryBuilders.boolQuery();
       // this.client = SpringContextUtil.getBean(TransportClient.class);
        //this.searchRequestBuilder = client.prepareSearch();
    }



    public EntityWrapper<T> eq(String column, Object params) {
        TermQueryBuilder whereAndCql =  QueryBuilders.termQuery(column,params);
        this.boolQueryBuilder.must(whereAndCql);
        return this;
    }


    /**
     * <p>
     * 添加OR条件
     * </p>
     */
    public EntityWrapper<T> or(String column, Object params) {
        TermQueryBuilder whereOrCql =  QueryBuilders.termQuery(column,params);
        this.boolQueryBuilder.should(whereOrCql);
        return this;
    }



    /**
     * <p>
     * 使用OR换行，并添加一个带()的新的条件
     * </p>
     * <p>
     * eg: ew.where("name='zhangsan'").and("id=11").orNew("statu=1"); 输出： WHERE
     * (name='zhangsan' AND id=11) OR (statu=1)
     * </p>
     *
     */
/*    public EntityWrapper<T> orNew(String column, Object params) {
        BoolQueryBuilder orBoolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder whereOrCql =  QueryBuilders.termQuery(column,params);
        orBoolQueryBuilder.must(whereOrCql);
        this.boolQueryBuilder.should(orBoolQueryBuilder);
        return this;
    }*/


    /**
     * <p>
     * 等同于SQL的"field>value"表达式
     * </p>
     *
     * @param column
     * @param params
     * @return
     */
    public EntityWrapper<T> gt(String column, Object params) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(column);
        rangeQueryBuilder.gt(params);
        this.boolQueryBuilder.must(rangeQueryBuilder);
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field>=value"表达式
     * </p>
     *
     * @param column
     * @param params
     * @return
     */
    public EntityWrapper<T> ge(String column, Object params) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(column);
        rangeQueryBuilder.gte(params);
        this.boolQueryBuilder.must(rangeQueryBuilder);
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field<value"表达式
     * </p>
     *
     * @param column
     * @param params
     * @return
     */
    public EntityWrapper<T> lt(String column, Object params) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(column);
        rangeQueryBuilder.lt(params);
        this.boolQueryBuilder.must(rangeQueryBuilder);
        return this;
    }

    /**
     * <p>
     * 等同于SQL的"field<=value"表达式
     * </p>
     *
     * @param column
     * @param params
     * @return
     */
    public EntityWrapper<T> le(String column, Object params) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(column);
        rangeQueryBuilder.lte(params);
        //或者
       // rangeQueryBuilder.to(params,Boolean.TRUE);
        this.boolQueryBuilder.must(rangeQueryBuilder);
        return this;
    }


    public EntityWrapper<T> groupBy(String columns) {

        return this;
    }


    public EntityWrapper<T> like(String column, String value) {
        // 根据 词 不分隔模糊匹配
        MatchPhraseQueryBuilder whereLikeCql= QueryBuilders.matchPhraseQuery(column,value);
        this.boolQueryBuilder.must(whereLikeCql);
        return this;
    }

    public EntityWrapper<T> in(String column, Collection<?> value) {
        TermsQueryBuilder termQueryBuilder = QueryBuilders.termsQuery(column,value);
        this.boolQueryBuilder.must(termQueryBuilder);
        return this;
    }

    public EntityWrapper<T> ids(String... id){
        IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery();
        idsQueryBuilder.addIds(id);
        this.boolQueryBuilder.must(idsQueryBuilder);
        return this;
    }

    public EntityWrapper<T> orderBy(String columns, Boolean isAsc) {
        this.searchRequestBuilder.addSort(columns, isAsc? SortOrder.ASC : SortOrder.DESC);
        return this;
    }

}
