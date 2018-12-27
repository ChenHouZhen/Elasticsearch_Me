package com.chenhz.transportclientelasticsearch.controller;


import com.chenhz.transportclientelasticsearch.entity.R;
import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "测试")
public class Test {

    @GetMapping("/test/t")
    public R test(){
        T t = new T();
        t.setDd(Long.MAX_VALUE-10);
        System.out.println(Long.MAX_VALUE-10);
        return R.ok().put("data",t);
    }
}

@Data
class T{
    private Long dd;
}
