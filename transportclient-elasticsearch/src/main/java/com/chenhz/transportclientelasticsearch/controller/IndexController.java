package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.utils.EsIndexUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "索引")
@RequestMapping("/index")
@RestController
public class IndexController {

    @Autowired
    EsIndexUtils esIndexUtils;

    @PostMapping("/add")
    @ApiOperation("新增")
    public void add(@RequestParam String indexName){
        esIndexUtils.createIndex(indexName);
    }

    @PostMapping("/delete")
    @ApiOperation("删除")
    public void delete(@RequestParam String indexName){
        esIndexUtils.deleteIndex(indexName);
    }

    @GetMapping("/isExist")
    @ApiOperation("是否存在")
    public boolean isExist(@RequestParam String indexName){
        return esIndexUtils.isExistIndex(indexName);
    }

}
