package com.cyh.haveaclass.logic;
import com.cyh.haveaclass.core.Lesson;
import com.cyh.haveaclass.core.LessonPlan;

import java.util.ArrayList;

/**
 * Created by CYH on 2018/2/10.
 */

public interface FunctionsInterface {
    /**
     * 该方法实现用网络爬虫获取课表
     * @param plan 课表里所有课程
     */
    void getClassScheduleFromInternet(LessonPlan plan);

    /**
     * 该方法通过用户输入获取课表
     * @param lesson 课表里所有课
     */
    void getClassScheduleFromInput(Lesson lesson);

    /**
     * 该方法获取距当前时间最近的下节课
     * @return 下节课名及其时间地点
     */
    Lesson getRecentLesson();

    /**
     * 该方法实现自动提醒功能
     */
    void notifyAutomatically();
}
