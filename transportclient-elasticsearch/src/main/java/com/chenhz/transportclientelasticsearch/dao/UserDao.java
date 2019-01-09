package com.chenhz.transportclientelasticsearch.dao;

import com.chenhz.transportclientelasticsearch.entity.User;
import com.chenhz.transportclientelasticsearch.utils.EntityWrapper;
import com.chenhz.transportclientelasticsearch.utils.JsonUtils;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
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
        BoolQueryBuilder sexBoolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("sex",sex);
        sexBoolQueryBuilder.must(termQueryBuilder);

        BoolQueryBuilder phoneAndNameBoolQueryBuilder = QueryBuilders.boolQuery();
        TermQueryBuilder phoneTermQueryBuilder = QueryBuilders.termQuery("phone",phone);
        TermQueryBuilder nameTermQueryBuilder = QueryBuilders.termQuery("name.keyword",name);
        phoneAndNameBoolQueryBuilder.should(phoneTermQueryBuilder);
        phoneAndNameBoolQueryBuilder.should(nameTermQueryBuilder);


        BoolQueryBuilder allBoolQueryBuilder = QueryBuilders.boolQuery();
        allBoolQueryBuilder.must(sexBoolQueryBuilder);
        allBoolQueryBuilder.must(phoneAndNameBoolQueryBuilder);

        return this.selectList(allBoolQueryBuilder);
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


    // where age > '' and age < ''
    public List<User> listByGtAndLtAge(Integer lAge,Integer hAge){
        return this.selectList(
                new EntityWrapper<User>().gt("age",lAge).lt("age",hAge)
        );
    }

    // where age >= '' and age <= ''
    public List<User> listByGteAndLteAge(Integer lAge,Integer hAge){
        return this.selectList(
                new EntityWrapper<User>().ge("age",lAge).le("age",hAge)
        );
    }

    // where age in ()
    public List<User> listByInAge(List<Integer> age){
        return this.selectList(
                new EntityWrapper<User>().in("age",age)
        );
    }


}
