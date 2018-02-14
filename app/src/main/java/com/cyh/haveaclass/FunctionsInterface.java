package com.cyh.haveaclass;

import com.cyh.haveaclass.core.AClass;

import java.util.ArrayList;

/**
 * Created by CYH on 2018/2/10.
 */

public interface FunctionsInterface {
    /**
     * 该方法实现用网络爬虫获取课表
     * @param classes 课表里所有课程
     */
    void getClassScheduleFromInternet(ArrayList<AClass> classes);

    /**
     * 该方法通过用户输入获取课表
     * @param classes 课表里所有课
     */
    void getClassScheduleFromInput(ArrayList<AClass> classes);

    /**
     * 该方法获取距当前时间最近的下节课
     * @return 下节课名及其时间地点
     */
    String getRecentClass();

    /**
     * 该方法实现自动提醒功能
     */
    void notifyAutomatically();
}
