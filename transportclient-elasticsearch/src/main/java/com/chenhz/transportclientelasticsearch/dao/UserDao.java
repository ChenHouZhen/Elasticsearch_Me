package com.chenhz.transportclientelasticsearch.dao;

import com.chenhz.transportclientelasticsearch.entity.User;
import com.chenhz.transportclientelasticsearch.utils.EntityWrapper;
import com.chenhz.transportclientelasticsearch.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserDao extends EsBaseDao<User>{


    public String addUser(User d){
        IndexResponse response = client.prepareIndex(getIndex(),getType())
                .setId(d.getId())
                .setSource(JsonUtils.toJsonStr(d),XContentType.JSON).get();
        return response.getId();
    }


    public List<User> listEqByName(String name){
        return this.selectList(
                new EntityWrapper<User>().eq("name.keyword",name)
        );
    }

    public List<User> listLikeByName(String name){
        return this.selectList(
                new EntityWrapper<User>().like("name",name)
        );
    }

    public User searchByPhone(String phone){
        return this.selectOne(
                new EntityWrapper<User>().eq("phone.keyword",phone)
        );
    }

    // where sex= 1 and (phone ="" or name = "")
    public List<User> listByNameOrNewPhoneSex(String name, String phone, Integer sex){
        return this.selectList(
                new EntityWrapper<User>().eq("name.keyword",name).orNew("phone",phone).eq("sex",sex)
        );
    }

    // where name = '' or phone = ''
    public List<User> listByNameOrPhone(String name,String phone){
        return this.selectList(
                new EntityWrapper<User>().or("name.keyword",name).or("phone",phone)
        );
    }

    // where name = '' and phone = ''
    public List<User> listByNameAndPhone(String name,String phone){
        return this.selectList(
                new EntityWrapper<User>().eq("name.keyword",name).eq("phone",phone)
        );
    }




}
