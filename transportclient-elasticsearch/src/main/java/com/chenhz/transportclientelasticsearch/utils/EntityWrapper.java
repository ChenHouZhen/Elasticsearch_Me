package com.chenhz.transportclientelasticsearch.utils;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;

import java.io.Serializable;

@Slf4j
public class EntityWrapper<T> implements Serializable {

    /**
     * ES 映射实体类
     */
   // protected T entity = null;

    protected BoolQueryBuilder boolQueryBuilder;


    public BoolQueryBuilder getBoolQueryBuilder() {
        return boolQueryBuilder;
    }

    public EntityWrapper() {
        init();
        /* 注意，传入查询参数 */
    }


    private void init(){
        // 初始化复杂查询构造器
        this.boolQueryBuilder = QueryBuilders.boolQuery();
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
    public EntityWrapper<T> orNew(String column, Object params) {
        BoolQueryBuilder orBoolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder whereOrCql =  QueryBuilders.termQuery(column,params);
        orBoolQueryBuilder.must(whereOrCql);
        this.boolQueryBuilder.should(orBoolQueryBuilder);
        return this;
    }


    public EntityWrapper<T> groupBy(String columns) {

        return this;
    }

    public EntityWrapper<T> orderBy(String columns) {

        return this;
    }

    public EntityWrapper<T> like(String column, String value) {
        // 根据 词 不分隔模糊匹配
        MatchPhraseQueryBuilder whereLikeCql= QueryBuilders.matchPhraseQuery(column,value);
        this.boolQueryBuilder.must(whereLikeCql);
        return this;
    }

    public EntityWrapper<T> in(String column, String value) {

        return this;
    }

}
