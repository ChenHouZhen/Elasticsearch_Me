package com.chenhz.transportclientelasticsearch.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chenhz.transportclientelasticsearch.config.IndexConfigProps;
import com.chenhz.transportclientelasticsearch.entity.Document;
import com.chenhz.transportclientelasticsearch.utils.EsQueryUtils;
import com.chenhz.transportclientelasticsearch.utils.JsonUtils;
import com.chenhz.transportclientelasticsearch.utils.PageUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author CHZ
 * @create 2018/12/23
 */
@Component
@Slf4j
public class DocumentDao {


    @Autowired
    private EsQueryUtils esQueryUtils;

    @Autowired
    private IndexConfigProps props;

    @Autowired
    private TransportClient client;

    // ------------------------------- 插入 ---------------------------------------

    public String createDocument(Document d){
        IndexResponse response = client.prepareIndex(props.getDoc().getName(),props.getDoc().getType())
                .setId(d.getId())
                .setSource(JsonUtils.toJsonStr(d),XContentType.JSON).get();
        return response.getId();
    }

    // ------------------------------- 更新 -----------------------------------------

    public String updateDocument(Document d) throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest(props.getDoc().getName(),
                props.getDoc().getType(),d.getId())
                .doc(JsonUtils.toJsonStr(d),XContentType.JSON);
        UpdateResponse response = client.update(request).get();
        return response.getId();
    }


    //-------------------------------- 查询 ---------------------------------------

    public Document searchById(String id){
        GetResponse response = client.prepareGet(props.getDoc().getName(),
                props.getDoc().getType(),
                id).get();
        return JSON.parseObject(response.getSourceAsString(),Document.class);
    }


    // 分页
    public PageUtils page(int limit ,int page){
        if (page <= 0){
            throw new IllegalArgumentException("页数不正确");
        }

        // 索引 和 类型
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(props.getDoc().getName());
        searchRequestBuilder.setTypes(props.getDoc().getType());

        // 分页
        searchRequestBuilder.setFrom(limit * (page - 1));
        searchRequestBuilder.setSize(limit);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();

        long total = searchResponse.getHits().totalHits;
        int currentSize = searchResponse.getHits().getHits().length;

        log.info("请求：{}",searchRequestBuilder.toString());
        log.info("响应：{}",searchResponse.toString());
        log.info("状态码：{}",searchResponse.status().getStatus());
        log.info("数据总数：{}，当前条数：{}",total,currentSize);

        List<Document> data = new ArrayList<>();
        for (SearchHit searchHit:searchResponse.getHits().getHits()) {
            Document t = JSON.parseObject(searchHit.getSourceAsString(),Document.class);
            t.setId(searchHit.getId());
            data.add(t);
        }

        PageUtils pageUtils = new PageUtils(data,total,currentSize,page);

        return pageUtils;
    }


    // (kgName = "kgName"  or kgId = kgID) and status = 1
    public PageUtils filter(String kgName,String kgId,Integer status){
        // 索引 和 类型
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(props.getDoc().getName());
        searchRequestBuilder.setTypes(props.getDoc().getType());
        //总
        BoolQueryBuilder allBoolQueryBuilder = QueryBuilders.boolQuery();

        // or
        BoolQueryBuilder orBoolQueryBuilder = QueryBuilders.boolQuery();
        if (!StringUtils.isEmpty(kgName)){

            // 根据 词 完整 匹配
            // TermQueryBuilder whereKgName =  QueryBuilders.termQuery("kgs.kgName.keyword",kgName);
            // 根据 字 分隔模糊匹配
            // MatchQueryBuilder whereKgName = QueryBuilders.matchQuery("kgs.kgName",kgName);

            // 比下面的厉害
            //MatchPhrasePrefixQueryBuilder
            // 根据 词 不分隔模糊匹配
            MatchPhraseQueryBuilder whereKgName = QueryBuilders.matchPhraseQuery("kgs.kgName",kgName);
            orBoolQueryBuilder.should(whereKgName);
        }
        if (!StringUtils.isEmpty(kgId)){
            TermQueryBuilder whereKgId = QueryBuilders.termQuery("kgs.kgId",kgId);
            orBoolQueryBuilder.should(whereKgId);
        }
        allBoolQueryBuilder.must(orBoolQueryBuilder);


        //and
        if (!StringUtils.isEmpty(status)){
            BoolQueryBuilder andBoolQueryBuilder = QueryBuilders.boolQuery();
            TermQueryBuilder whereStatus = QueryBuilders.termQuery("status",status);
            andBoolQueryBuilder.must(whereStatus);
            allBoolQueryBuilder.must(andBoolQueryBuilder);
        }


        searchRequestBuilder.setQuery(allBoolQueryBuilder);
        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();


        long total = searchResponse.getHits().totalHits;
        int currentSize = searchResponse.getHits().getHits().length;

        log.info("请求：{}",searchRequestBuilder.toString());
        log.info("条件：{}",allBoolQueryBuilder.toString());
        log.info("响应：{}",searchResponse.toString());
        log.info("状态码：{}",searchResponse.status().getStatus());
        log.info("数据总数：{}，当前条数：{}",total,currentSize);

        List<Document> data = new ArrayList<>();
        for (SearchHit searchHit:searchResponse.getHits().getHits()) {
            Document t = JSON.parseObject(searchHit.getSourceAsString(),Document.class);
            t.setId(searchHit.getId());
            data.add(t);
        }

        PageUtils pageUtils = new PageUtils(data,total,currentSize,1);

        return pageUtils;
    }


    // group by status  数量
    public JSONArray groupBy(String field, String order){
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch(props.getDoc().getName());
        searchRequestBuilder.setTypes(props.getDoc().getType());

        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders.terms("groupBy").field(field);
        searchRequestBuilder.addAggregation(termsAggregationBuilder);

        SearchResponse searchResponse = searchRequestBuilder.execute().actionGet();
        Aggregations aggregations = searchResponse.getAggregations();
        Aggregation aggregation = aggregations.get("groupBy");

        log.info("请求：{}",searchRequestBuilder.toString());
        log.info("响应：{}",searchResponse.toString());
        log.info("聚合：{}",aggregation.toString());

        JSONObject jsonObject = JSON.parseObject(aggregation.toString());
        JSONArray jsonArray = jsonObject.getJSONObject("groupBy").getJSONArray("buckets");

        return jsonArray;
    }

    // order by 时间


    // 时间筛选



    // ------------------------------------ 删除 --------------------------------------

    public void deleteDocument(String id){
        DeleteRequest request = new DeleteRequest(props.getDoc().getName(),
                props.getDoc().getType(),id);
        client.delete(request);
    }


    private void flush(){
        // 待实现
    }

}
