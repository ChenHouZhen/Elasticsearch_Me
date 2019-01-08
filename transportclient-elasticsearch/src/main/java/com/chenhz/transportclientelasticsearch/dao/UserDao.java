package com.chenhz.transportclientelasticsearch.dao;

import com.chenhz.transportclientelasticsearch.entity.User;
import com.chenhz.transportclientelasticsearch.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDao extends EsBaseDao<User>{

    @Autowired
    private TransportClient client;


    public String addUser(User d){
        IndexResponse response = client.prepareIndex("user","_doc")
                .setId(d.getId())
                .setSource(JsonUtils.toJsonStr(d),XContentType.JSON).get();
        return response.getId();
    }

    public static void main(String[] args) {
        UserDao e = new UserDao();
        System.out.println(e.initReflect());
    }
}
