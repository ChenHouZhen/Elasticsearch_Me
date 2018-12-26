package com.chenhz.transportclientelasticsearch.dao;

import com.alibaba.fastjson.JSON;
import com.chenhz.transportclientelasticsearch.config.IndexConfigProps;
import com.chenhz.transportclientelasticsearch.entity.Time;
import com.chenhz.transportclientelasticsearch.utils.EsQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.AbstractQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class TimeDao {

    @Autowired
    private EsQueryUtils esQueryUtils;

    @Autowired
    private IndexConfigProps props;

    @Autowired
    private TransportClient client;


    public String createTime(Time d) throws ExecutionException, InterruptedException {
        IndexRequest request = new IndexRequest(props.getTime().getName()
                ,props.getTime().getType());
        request.source(JSON.toJSON(d), XContentType.JSON);
        IndexResponse response = null;

        response = client.index(request).get();
/*        IndexResponse indexResponse = client
                .prepareIndex("map","ddd","2")
                .setSource(map)
                .get();*/
        return response.getId();
    }


    public String updateTime(Time d) throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest(props.getTime().getName(),
                props.getTime().getType(),d.getId())
                .doc(JSON.toJSON(d),XContentType.JSON);
        UpdateResponse response = client.update(request).get();
        return response.getId();
    }

    public List<Time> matchAllQuery(){
        List<Time> result = new ArrayList<>();

        result = getDocument(QueryBuilders.matchAllQuery());

        return result;
    }

    public List<Time> wildcardQuery(String q){
        List<Time> result = new ArrayList<>();

        result = getDocument(QueryBuilders.queryStringQuery("*" + q.toLowerCase() +" *"));

        return result;
    }

    public void DeleteDocument(String id){
        DeleteRequest request = new DeleteRequest(props.getTime().getName(),
                props.getTime().getType(),id);
        client.delete(request);
    }


    private List<Time> getDocument(AbstractQueryBuilder builder){
        List<Time> result = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder);
        SearchRequest searchRequest = new SearchRequest(props.getTime().getName(),
                props.getTime().getType());
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = (SearchResponse) client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits){
            Time t = JSON.parseObject(hit.getSourceAsString(),Time.class);
            t.setId(hit.getId());
            result.add(t);
        }

        return result;
    }

    private void flush(){
        // 待实现
    }

}
