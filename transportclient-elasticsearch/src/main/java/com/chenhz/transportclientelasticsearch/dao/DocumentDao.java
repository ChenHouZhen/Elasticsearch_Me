package com.chenhz.transportclientelasticsearch.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.chenhz.transportclientelasticsearch.config.IndexConfigProps;
import com.chenhz.transportclientelasticsearch.entity.Document;
import com.chenhz.transportclientelasticsearch.utils.EsQueryUtils;
import com.chenhz.transportclientelasticsearch.utils.JsonUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetResponse;
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
import springfox.documentation.spring.web.json.Json;

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

    public String createDocument(Document d){
        IndexResponse response = client.prepareIndex(props.getDoc().getName(),props.getDoc().getType())
                .setId(d.getId())
                .setSource(JsonUtils.toJsonStr(d),XContentType.JSON).get();
        return response.getId();
    }

    public Document searchById(String id){
        GetResponse response = client.prepareGet(props.getDoc().getName(),
                props.getDoc().getType(),
                id).get();
        return JSON.parseObject(response.getSourceAsString(),Document.class);
    }



    public String updateDocument(Document d) throws ExecutionException, InterruptedException {
        UpdateRequest request = new UpdateRequest(props.getDoc().getName(),
                props.getDoc().getType(),d.getId())
                .doc(JsonUtils.toJsonStr(d),XContentType.JSON);
        UpdateResponse response = client.update(request).get();
        return response.getId();
    }

    public List<Document> matchAllQuery(){
        List<Document> result = new ArrayList<>();

        result = getDocument(QueryBuilders.matchAllQuery());

        return result;
    }

    public List<Document> wildcardQuery(String q){
        List<Document> result = new ArrayList<>();

        result = getDocument(QueryBuilders.queryStringQuery("*" + q.toLowerCase() +" *"));

        return result;
    }

    public void DeleteDocument(String id){
        DeleteRequest request = new DeleteRequest(props.getDoc().getName(),
                props.getDoc().getType(),id);
        client.delete(request);
    }


    private List<Document> getDocument(AbstractQueryBuilder builder){
        List<Document> result = new ArrayList<>();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder);
        SearchRequest searchRequest = new SearchRequest(props.getDoc().getName(),
                props.getDoc().getType());
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = (SearchResponse) client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits){
            Document t = JSON.parseObject(hit.getSourceAsString(),Document.class);
            t.setId(hit.getId());
            result.add(t);
        }

        return result;
    }

    private void flush(){
        // 待实现
    }

}
