package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.dao.DocumentDao;
import com.chenhz.transportclientelasticsearch.entity.Document;
import com.chenhz.transportclientelasticsearch.utils.UUIDGenerate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("doc")
@Api(tags = "Document")
public class DocumentController {

    @Autowired
    DocumentDao documentDao;


    @GetMapping("/info")
    public Document info(String id){
        return null;
    }


    @GetMapping("/all")
    @ApiModelProperty("查询全部")
    public List<Document> all(){
        return documentDao.matchAllQuery();
    }

    @PostMapping("/add")
    @ApiModelProperty("新增")
    public Document add(Document t){
        t.setId(UUIDGenerate.create());
        t.setUpdateTime(new Date());
        t.setCreateTime(new Date());
        return t;
    }
}
