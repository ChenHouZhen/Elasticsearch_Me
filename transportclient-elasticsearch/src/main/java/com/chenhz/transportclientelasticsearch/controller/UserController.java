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
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
    @ApiOperation("根据名称精确查")
    public R listEq(String name){
        return R.ok().put("data",userDao.listEqByName(name));
    }

    @GetMapping("/list/like")
    @ApiOperation("根据名称模糊查")
    public R listLike(String name){
        return R.ok().put("data",userDao.listLikeByName(name));
    }

    @GetMapping("/info/phone")
    @ApiOperation("根据手机号精确查")
    public R infoPhone(String phone){
        return R.ok().put("data",userDao.searchByPhone(phone));
    }

    @GetMapping("/list/ornew")
    @ApiOperation("根据名称、手机、 性别   精确查 ")
    public R listOrNew(String name,String phone,Integer sex){
        return R.ok().put("data",userDao.listByNameOrNewPhoneSex(name,phone,sex));
    }

    @GetMapping("/list/or")
    @ApiOperation("根据名称、手机 or  精确查 ")
    public R listOr(String name,String phone){
        return R.ok().put("data",userDao.listByNameOrPhone(name,phone));
    }

    @GetMapping("/list/and")
    @ApiOperation("根据名称、手机 and  精确查 ")
    public R listAnd(String name,String phone){
        return R.ok().put("data",userDao.listByNameAndPhone(name,phone));
    }


    @GetMapping("/list/gtAndLt")
    @ApiOperation("根据年齡 gt lt 查 ")
    public R listGtAndLt(Integer lAge,Integer hAge){
        return R.ok().put("data",userDao.listByGtAndLtAge(lAge,hAge));
    }

    @GetMapping("/list/gteAndLte")
    @ApiOperation("根据年齡 gte lte 查 ")
    public R listGteAndLte(Integer lAge,Integer hAge){
        return R.ok().put("data",userDao.listByGteAndLteAge(lAge,hAge));
    }

    @GetMapping("/list/ids")
    @ApiOperation("根据ID列表查 ")
    public R listByIds(@RequestParam String[] ids){
        return R.ok().put("data",userDao.listByIds(ids));
    }


    @GetMapping("/list/ages")
    @ApiOperation("根据年龄列表查 ")
    public R listByAges(@RequestParam Integer[] ages){
        return R.ok().put("data",userDao.listByInAge(Arrays.asList(ages)));
    }

    @GetMapping("/order/ages")
    @ApiOperation("根据年龄列表查 ")
    public R orderByAges(boolean isAsc){
        return R.ok().put("data",userDao.listOrderByAge(isAsc));
    }

    @GetMapping("/page/like/phone")
    @ApiOperation("根据手机模糊查 ")
    public R pageLikePhone(String phone,int page,int size){
        return R.ok().put("data",userDao.pageByListPhone(phone,page,size));
    }

    @GetMapping("/page")
    @ApiOperation("分页查全部 ")
    public R page(int page,int size){
        return R.ok().put("data",userDao.page(page,size));
    }

    @PostMapping("/add")
    @ApiOperation("索引新数据")
    public R add(User user){
        user.setId(UUIDGenerate.create());
        userDao.addUser(user);
        return R.ok().put("data",user);
    }


    @PostMapping("/test")
    @ApiOperation("test")
    public R test(@RequestBody User user){
        return R.ok();
    }

}
