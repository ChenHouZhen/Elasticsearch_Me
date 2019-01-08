package com.chenhz.transportclientelasticsearch.controller;

import com.chenhz.transportclientelasticsearch.dao.UserDao;
import com.chenhz.transportclientelasticsearch.entity.R;
import com.chenhz.transportclientelasticsearch.entity.User;
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


    @GetMapping("/info/id")
    public R info(String id){
        return R.ok().put("data",userDao.selectById(id));
    }

    @GetMapping("/generate")
    @ApiOperation(value = "随机生成 User 对象")
    public R generate(){
        List<User> docs = userGenerator.create();
        docs.forEach(i -> userDao.addUser(i));
        return R.ok().put("data",docs);
    }

    @GetMapping("/list/eq")
    @ApiModelProperty("根据名称精确查")
    public R listEq(String name){
        return R.ok().put("data",userDao.listEqByName(name));
    }

    @GetMapping("/list/like")
    @ApiModelProperty("根据名称模糊查")
    public R listLike(String name){
        return R.ok().put("data",userDao.listLikeByName(name));
    }

    @GetMapping("/info/phone")
    @ApiModelProperty("根据手机号精确查")
    public R infoPhone(String phone){
        return R.ok().put("data",userDao.searchByPhone(phone));
    }

    @GetMapping("/list/ornew")
    @ApiModelProperty("根据名称、手机 orNew  精确查 ")
    public R listOrNew(String name,String phone,Integer sex){
        return R.ok().put("data",userDao.listByNameOrNewPhoneSex(name,phone,sex));
    }

    @GetMapping("/list/or")
    @ApiModelProperty("根据名称、手机 or  精确查 ")
    public R listOr(String name,String phone){
        return R.ok().put("data",userDao.listByNameOrPhone(name,phone));
    }

    @GetMapping("/list/and")
    @ApiModelProperty("根据名称、手机 and  精确查 ")
    public R listAnd(String name,String phone){
        return R.ok().put("data",userDao.listByNameAndPhone(name,phone));
    }

    @PostMapping("/add")
    @ApiModelProperty("索引新数据")
    public R add(User user){
        user.setId(UUIDGenerate.create());
        userDao.addUser(user);
        return R.ok().put("data",user);
    }

}
