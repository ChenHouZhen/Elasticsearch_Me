package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.dao.DocumentDao;
import com.chenhz.transportclientelasticsearch.entity.Document;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
