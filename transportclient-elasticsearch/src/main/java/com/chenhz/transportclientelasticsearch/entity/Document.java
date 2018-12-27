package com.chenhz.transportclientelasticsearch.entity;
/*
 * Created by hakdogan on 01/12/2017
 */

import com.chenhz.transportclientelasticsearch.utils.TimeHelper;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@ApiModel
@Getter
@Setter
@ToString
public class Document {
    private String id;
    private String title;
    private String subject;
    private String content;
    private Date createTime;
    private String updateTime;

    private List<Node> kgs;

    public void setUpdateTime(Date d){
       updateTime = TimeHelper.dateToStr(d);
    }
}
