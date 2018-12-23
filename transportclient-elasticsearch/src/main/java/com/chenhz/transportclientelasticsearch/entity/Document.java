package com.chenhz.transportclientelasticsearch.entity;
/*
 * Created by hakdogan on 01/12/2017
 */

import lombok.*;

@Data
public class Document {
    private String id;
    private String title;
    private String subject;
    private String content;
    private Data createTime;
}
