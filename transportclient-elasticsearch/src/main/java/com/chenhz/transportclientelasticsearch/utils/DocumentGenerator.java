package com.chenhz.transportclientelasticsearch.utils;

import com.chenhz.transportclientelasticsearch.entity.Document;
import com.chenhz.transportclientelasticsearch.entity.Node;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author CHZ
 * @create 2018/12/30
 */
@Component
public class DocumentGenerator {

    @Autowired
    private Faker faker;

    @Autowired
    private Generator generator;

    private final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ,.";

    private final String nums = "0123456789";

    private final String statuses = "012";

    public  List<Document> create(){
        return IntStream.rangeClosed(1,20)
                .mapToObj(i -> {
                    Document d = new Document();
                    d.setId(UUIDGenerate.create());
                    d.setCreateTime(new Date(System.currentTimeMillis() - Integer.valueOf(generator.generateRandomChars(nums,8))));
                    d.setUpdateTime(new Date(System.currentTimeMillis() - Integer.valueOf(generator.generateRandomChars(nums,9))));
                    d.setContent(generator.generateRandomChars(chars,50));
                    d.setTitle(generator.generateRandomChars(chars,8));
                    d.setStatus(Integer.valueOf(generator.generateRandomChars(statuses,1)));

                    List<Node> kgs = new ArrayList<>();
                    Node n1 = new Node();
                    n1.setKgId(UUIDGenerate.create());
                    n1.setKgName(faker.name().name());
                    Node n2 = new Node();
                    n2.setKgId(UUIDGenerate.create());
                    n2.setKgName(faker.name().name());
                    kgs.add(n1);
                    kgs.add(n2);
                    d.setKgs(kgs);

                    return d;
                }).collect(Collectors.toList());
    }

}
