package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.dao.DocumentDao;
import com.chenhz.transportclientelasticsearch.entity.Document;
import com.chenhz.transportclientelasticsearch.entity.Node;
import com.chenhz.transportclientelasticsearch.entity.R;
import com.chenhz.transportclientelasticsearch.utils.DocumentGenerator;
import com.chenhz.transportclientelasticsearch.utils.UUIDGenerate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("doc")
@Api(tags = "Document")
public class DocumentController {

    @Autowired
    DocumentDao documentDao;


    @Autowired
    private DocumentGenerator documentGenerator;



    @GetMapping("/info")
    public Document info(String id){
        return documentDao.searchById(id);
    }

    @GetMapping("/generate")
    @ApiOperation(value = "随机生成 Document 对象")
    public R generate(){
        List<Document> docs = documentGenerator.create();
        docs.forEach(i -> documentDao.createDocument(i));
        return R.ok().put("data",docs);
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

        List<Node> kgs = new ArrayList<>();
        Node n1 = new Node();
        n1.setKgId(UUIDGenerate.create());
        n1.setKgName("知识点1");

        Node n2 = new Node();
        n2.setKgId(UUIDGenerate.create());
        n2.setKgName("知识点2");
        kgs.add(n1);
        kgs.add(n2);

        t.setKgs(kgs);

        t.setId(documentDao.createDocument(t));
        return t;
    }
}
