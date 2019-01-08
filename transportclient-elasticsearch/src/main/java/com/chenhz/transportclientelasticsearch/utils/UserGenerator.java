package com.chenhz.transportclientelasticsearch.utils;

import com.chenhz.transportclientelasticsearch.entity.User;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class UserGenerator {
    @Autowired
    private Faker faker;

    @Autowired
    private Generator generator;

    private final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.";

    private final String nums = "0123456789";

    private final String statuses = "012";

    public List<User> create(){
        return IntStream.rangeClosed(1,20)
                .mapToObj(i -> {
                    User d = new User();
                    d.setId(UUIDGenerate.create());
                    d.setName(faker.name().fullName());
                    d.setEmail(faker.internet().emailAddress(generator.generateRandomChars(nums,9)));
                    d.setAge(Integer.valueOf(generator.generateRandomChars(nums,2)));
                    d.setSex(generator.generateRandomInteger(0,2));
                    d.setPhone(faker.phoneNumber().cellPhone());
                    return d;
                }).collect(Collectors.toList());
    }

}
