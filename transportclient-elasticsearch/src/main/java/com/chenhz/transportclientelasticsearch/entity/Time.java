package com.chenhz.transportclientelasticsearch.entity;

/**
 * @author CHZ
 * @create 2018/12/23
 */

import com.chenhz.transportclientelasticsearch.utils.TimeHelper;
import lombok.Getter;

import java.util.Date;

/**
 * 测试 格式化后的时间是否能在 es 中排序、范围取值、筛选
 */
@Getter
public class Time {
    private String updateTime;

    public void setUpdateTime(Date updateTime) {
        this.updateTime = TimeHelper.dateToStr(updateTime);
    }

}
