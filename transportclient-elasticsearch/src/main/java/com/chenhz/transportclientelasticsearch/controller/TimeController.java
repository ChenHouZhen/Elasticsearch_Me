package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.dao.TimeDao;
import com.chenhz.transportclientelasticsearch.entity.Time;
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
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/time")
@Api(tags = "Time")
public class TimeController {

    @Autowired
    private TimeDao timeDao;

    @GetMapping("/all")
    @ApiModelProperty("查询全部")
    public List<Time> all(){
        return timeDao.matchAllQuery();
    }

    @PostMapping("/add")
    @ApiModelProperty("新增")
    public Time add(Time t) throws ExecutionException, InterruptedException {
        t.setId(UUIDGenerate.create());
        t.setUpdateTime(new Date());
        t.setCreateTime(new Date());
        timeDao.createTime(t);
        return t;
    }
}
