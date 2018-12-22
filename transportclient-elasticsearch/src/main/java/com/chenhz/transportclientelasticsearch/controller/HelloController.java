package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.config.EsConfigProps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Api(tags = "测试类")
@Slf4j
public class HelloController {

    @Autowired
    EsConfigProps configProps;

    @GetMapping("/hi")
    @ApiOperation(value = "HI")
    public String hi(){
        return configProps.toString();
    }
}
