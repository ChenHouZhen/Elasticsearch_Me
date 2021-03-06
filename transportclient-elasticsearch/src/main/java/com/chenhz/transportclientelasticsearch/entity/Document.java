package com.chenhz.transportclientelasticsearch.entity;
/*
 * Created by hakdogan on 01/12/2017
 */

import com.chenhz.transportclientelasticsearch.utils.TimeHelper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "标题")
    private String title;
    private String content;

    private Integer status;

    private Date createTime;
    private String updateTime;

    private List<Node> kgs;

    public void setUpdateTime(Date d){
       updateTime = TimeHelper.dateToStr(d);
    }
}
