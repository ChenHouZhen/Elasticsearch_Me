package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.utils.EsIndexUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "索引")
@RequestMapping("/index")
@RestController
public class IndexController {


    @Autowired
    EsIndexUtils esIndexUtils;

    @PostMapping("add")
    public void add(String indexName){
        esIndexUtils.createIndex(indexName);
    }

    @PostMapping("delete")
    public void delete(String indexName){
        esIndexUtils.deleteIndex(indexName);
    }

    public boolean isExist(String indexName){
        return esIndexUtils.isExistIndex(indexName);
    }

}
