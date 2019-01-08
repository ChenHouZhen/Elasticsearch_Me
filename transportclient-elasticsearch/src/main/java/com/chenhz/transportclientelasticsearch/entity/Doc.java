package com.chenhz.transportclientelasticsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author CHZ
 * @create 2018/12/23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Doc {
    private String name;
    private String type;
}
