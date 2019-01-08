package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.dao.UserDao;
import com.chenhz.transportclientelasticsearch.entity.R;
import com.chenhz.transportclientelasticsearch.entity.User;
import com.chenhz.transportclientelasticsearch.utils.EntityWrapper;
import com.chenhz.transportclientelasticsearch.utils.UUIDGenerate;
import com.chenhz.transportclientelasticsearch.utils.UserGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("user")
@Api(tags = "User数据")
public class UserController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserGenerator userGenerator;


    @GetMapping("/info")
    public R info(String id){
//        return userDao.searchById(id);
        return null;
    }

    @GetMapping("/generate")
    @ApiOperation(value = "随机生成 User 对象")
    public R generate(){
        List<User> docs = userGenerator.create();
        docs.forEach(i -> userDao.addUser(i));
        return R.ok().put("data",docs);
    }

    @GetMapping("/list")
    @ApiModelProperty("分页")
    public R list(String name){
        List<User> users = userDao.selectList(
                new EntityWrapper<User>(new User()).eq("name.keyword",name)
        );
        return R.ok().put("data",users);
    }

    @PostMapping("/add")
    @ApiModelProperty("索引新数据")
    public R add(User user){
        user.setId(UUIDGenerate.create());
        userDao.addUser(user);
        return R.ok().put("data",user);
    }



}
