package com.chenhz.transportclientelasticsearch.entity;


import com.chenhz.transportclientelasticsearch.annotation.EsDocument;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;

@EsDocument(index = "user",type = "_doc")
@Data
@ApiModel
public class User {

    private String id;

    private String name;

    private String email;

    private String phone;

    private Integer age;

    private Integer sex;
}
