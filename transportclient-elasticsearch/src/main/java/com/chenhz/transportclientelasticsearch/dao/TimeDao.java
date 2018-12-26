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
import java.util.List;

@Component
@Slf4j
public class TimeDao {

    @Autowired
    private EsQueryUtils esQueryUtils;

    @Autowired
    private IndexConfigProps props;

    @Autowired
    private TransportClient client;


    public String createTime(Time d){
        IndexRequest request = new IndexRequest(props.getIndexTime().getName()
                ,props.getIndexTime().getType());
        request.source(JSON.toJSON(d),XContentType.JSON);
        IndexResponse response = (IndexResponse) client.index(request);
        return response.getId();
    }


    public String updateTime(Time d){
        UpdateRequest request = new UpdateRequest(props.getIndexTime().getName(),
                props.getIndexTime().getType(),d.getId())
                .doc(JSON.toJSON(d),XContentType.JSON);
        UpdateResponse response = (UpdateResponse) client.update(request);
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
        DeleteRequest request = new DeleteRequest(props.getIndexTime().getName(),
                props.getIndexTime().getType(),id);
        client.delete(request);
    }


    private List<Time> getDocument(AbstractQueryBuilder builder){
        List<Time> result = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder);
        SearchRequest searchRequest = new SearchRequest(props.getIndexTime().getName(),
                props.getIndexTime().getType());
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
