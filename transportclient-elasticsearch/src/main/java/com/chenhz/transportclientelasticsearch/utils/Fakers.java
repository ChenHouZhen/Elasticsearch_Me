package com.chenhz.transportclientelasticsearch.utils;

import com.github.javafaker.Address;
import com.github.javafaker.Book;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Fakers {

    static Logger log = LoggerFactory.getLogger(Fakers.class);

    @Autowired
    private Faker faker;

    /**
     * 地址信息类
     */
    public void address(){
        log.info("地址API...");
        Address address = faker.address();
        log.info("address.city():"+address.city());  // 市
        log.info("address.latitude():"+address.latitude());
        log.info("address.longitude():"+address.longitude());
        log.info("address.streetAddress():"+address.streetAddress()); //地址
        log.info("address.cityName():"+address.cityName());
        log.info("address.state():"+address.state());  //省

/*        log.info("address.country():"+address.country());
        log.info("address.countryCode():"+address.countryCode());
        log.info("address.firstName():"+address.firstName());
        log.info("address.lastName():"+address.lastName());
        log.info("address.secondaryAddress():"+address.secondaryAddress());
        log.info("address.stateAbbr():"+address.stateAbbr());
        log.info("address.streetAddressNumber():"+address.streetAddressNumber());
        log.info("address.streetName():"+address.streetName());
        log.info("address.timeZone():"+address.timeZone());
        log.info("address.zipCode():"+address.zipCode());*/
        log.info("-------------------------------");
    }

    /**
     * 名字
     */
    public void name(){
        log.info("名字API......");
        Name name = faker.name();
        log.info("name.name()："+name.name());
        log.info("name.fullName()："+name.fullName());
        log.info("name.lastName()："+name.lastName());
        log.info("name.firstName()："+name.firstName());

/*        log.info("name.username()："+name.username());
        log.info("name.nameWithMiddle()："+name.nameWithMiddle());
        log.info("name.prefix()："+name.prefix());
        log.info("name.suffix()："+name.suffix());
        log.info("name.title()："+name.title());*/
        log.info("-------------------------------");
    }


    public void book(){
        Book book = faker.book();
        log.info("书籍API......");
        log.info("book.author():"+book.author());
        log.info("book.genre():"+book.genre());
        log.info("book.publisher():"+book.publisher());
        log.info("book.title():"+book.title());
        log.info("-------------------------------");
    }


}
