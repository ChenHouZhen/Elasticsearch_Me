package com.chenhz.transportclientelasticsearch.dao;

import com.chenhz.transportclientelasticsearch.utils.EsQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author CHZ
 * @create 2018/12/23
 */
@Component
@Slf4j
public class DocumentDao {

    @Autowired
    private EsQueryUtils esQueryUtils;
}
