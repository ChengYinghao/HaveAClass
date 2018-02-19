package com.cyh.haveaclass.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cyh.haveaclass.R;
import com.cyh.haveaclass.core.Lesson;
import com.cyh.haveaclass.core.PlanUtils;
import com.cyh.haveaclass.core.WebSitePlan;

import java.util.Calendar;
import java.util.Collection;


public class MainActivity extends AppCompatActivity {
    WebSitePlan webSitePlan;
    Button button;
    TextView display;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webSitePlan = new WebSitePlan("150Б52");
        button = findViewById(R.id.nextLesson_display_button);
        display = findViewById(R.id.nextLesson_display_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int day = PlanUtils.INSTANCE.nowDayOfWeek(calendar);
                int week = PlanUtils.INSTANCE.nowWeekType(calendar);
                int section = PlanUtils.INSTANCE.nowSection(calendar);
                int nextSection = section + 1;
                if (nextSection >= 8) {
                    display.setText("今天没课啦，玩去吧！");
                    return;
                }
                Collection<Lesson> nextLessons = webSitePlan.selectLessonsBySection(week, day, section + 1);
                int size = nextLessons.size();
                if (size == 0) {
                    display.setText("下节没课，开心吧");
                } else {
                    String nextLesson = "";
                    for (Lesson lesson : nextLessons) {
                        nextLesson = displayLesson(lesson) + ";" + nextLesson;
                    }
                    display.setText(nextLesson);
                }
            }
        });
    }

    String displayLesson(Lesson lesson) {
        String place = lesson.getPlace();
        String name = lesson.getName();
        String teacher = lesson.getTeacher();
        int section = lesson.getSection();
        String time;
        switch (section) {
            case 1:
                time = "8:30";
                break;
            case 2:
                time = "10:25";
                break;
            case 3:
                time = "12:20";
                break;
            case 4:
                time = "14:15";
                break;
            case 5:
                time = "16:10";
                break;
            case 6:
                time = "18:05";
                break;
            default:
                time = "休息时间";
                break;
        }
        String result = "下节课是" + name + ", " + time + "在" + place + "上课, " + "老师是" + teacher;
        return result;
    }


}
