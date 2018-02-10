package com.cyh.haveaclass;

import java.util.ArrayList;

/**
 * Created by CYH on 2018/2/10.
 */

public interface Functions {
    /**
     * 该方法实现用网络爬虫获取课表
     * @param times 上课的时间（几点，不包含周几等信息）
     * @param sites 上课地点
     * @param whatDay 周几
     * @param oddWeek 是否是单周
     * @param whatClass 什么课
     */
    void getClassScheduleFromInternet(ArrayList<String> times, ArrayList<String> sites, ArrayList<String> whatDay, ArrayList<Boolean> oddWeek,
                                      ArrayList<String> whatClass);

    /**
     * 该方法通过用户输入获取课表
     * @param times 上课的时间（几点，不包含周几等信息）
     * @param sites 上课地点
     * @param whatDay 周几
     * @param oddWeek 是否是单周
     * @param whatClass 什么课
     */
    void getClassScheduleFromInput(ArrayList<String> times, ArrayList<String> sites, ArrayList<String> whatDay, ArrayList<Boolean> oddWeek,
                                   ArrayList<String> whatClass);

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
