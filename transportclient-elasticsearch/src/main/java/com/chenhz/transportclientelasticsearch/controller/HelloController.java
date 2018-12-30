package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.config.EsConfigProps;
import com.chenhz.transportclientelasticsearch.config.IndexConfigProps;
import com.chenhz.transportclientelasticsearch.entity.R;
import com.chenhz.transportclientelasticsearch.utils.DocumentGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.soap.Text;
import java.util.Map;

@RestController
@Api(tags = "测试类")
@Slf4j
public class HelloController {

    @Autowired
    EsConfigProps configProps;

    @Autowired
    IndexConfigProps indexConfigProps;

    @Autowired
    private DocumentGenerator documentGenerator;

    @GetMapping("/hi")
    @ApiOperation(value = "HI")
    public String hi(){
        return configProps.toString() + "     " +
                "" +indexConfigProps.toString();
    }


    @GetMapping("/document")
    @ApiOperation(value = "随机获取 Document 对象")
    public R documents(){
        return R.ok().put("data",documentGenerator.create());
    }


}
