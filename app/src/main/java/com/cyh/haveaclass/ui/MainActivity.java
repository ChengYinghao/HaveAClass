package com.cyh.haveaclass.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.cyh.haveaclass.R;
import com.cyh.haveaclass.core.Lesson;
import com.cyh.haveaclass.core.LessonPlan;
import com.cyh.haveaclass.logic.FunctionsInterface;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements FunctionsInterface {
    SQLiteDatabase database;
    private final String TABLE_NAME = "ClassScheduleTable";
    private final String PATH = "ClassScheduleDataBase";
    private final String COLUMN_NAME = "name";
    private final String COLUMN_TYPE = "type";
    private final String COLUMN_PLACE = "place";
    private final String COLUMN_WEEK = "week";
    private final String COLUMN_DAY = "day";
    private final String COLUMN_SECTION = "section";
    private final String COLUMN_TEACHER = "teacher";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = SQLiteDatabase.openOrCreateDatabase(PATH, null);
        createTable(database);
    }

    void createTable(SQLiteDatabase database) {
        String create = "CREATE TABLE " + TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, )"
                + COLUMN_NAME + " TEXT, " + COLUMN_TYPE + " TEXT, " + COLUMN_PLACE + " TEXT, "
                + COLUMN_WEEK + " INTEGER, " + COLUMN_DAY + " INTEGER, " + COLUMN_SECTION +
                " INTEGER, " + COLUMN_TEACHER + " TEXT);";
        database.execSQL(create);
    }

    /**
     * 该方法用于插入单条课程记录
     *
     * @param lesson
     */
    void insertLesson(Lesson lesson) {
        String sql = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME + "," + COLUMN_TYPE + "," + COLUMN_PLACE + "," + COLUMN_WEEK + ","
                + COLUMN_DAY + "," + COLUMN_SECTION + "," + COLUMN_TEACHER + ")" + "\rVALUES(" + lesson.getName() + ", " + lesson.getType()
                + ", " + lesson.getPlace() + ", " + lesson.getWeek() + ", " + lesson.getDay() + ", " + lesson.getSection() + ", " + lesson.getTeacher() + " );";
        database.execSQL(sql);
    }

    void insertLessons(LessonPlan plan) {
        for (Lesson lesson : plan.allLessons()) {
            insertLesson(lesson);
        }
    }

    @Override
    public void getClassScheduleFromInternet(LessonPlan plan) {
        insertLessons(plan);
    }

    @Override
    public void getClassScheduleFromInput(Lesson lesson) {
        insertLesson(lesson);
    }

    @Override
    public Lesson getRecentLesson() {
        Calendar calendar = Calendar.getInstance();
        final Integer week = getWeek(calendar);
        final Integer day = getDay(calendar);
        final Integer time = getTime(calendar);
        Cursor cursor = database.query(TABLE_NAME, new String[]{COLUMN_NAME, COLUMN_TYPE, COLUMN_PLACE, COLUMN_WEEK, COLUMN_DAY,
                        COLUMN_SECTION, COLUMN_TEACHER}, COLUMN_DAY + "=?, " + COLUMN_WEEK + "=?, " + COLUMN_SECTION + "=?",
                (String[]) new Object[]{day, week, time}, null, null, null);
        final String name = cursor.getString(2);
        final String type = cursor.getString(3);
        final String place = cursor.getString(4);
        final String techer = cursor.getString(8);
        Lesson lesson = new Lesson() {
            @NotNull
            @Override
            public String getName() {
                return name;
            }

            @NotNull
            @Override
            public String getType() {
                return type;
            }

            @NotNull
            @Override
            public String getPlace() {
                return place;
            }

            @Override
            public int getWeek() {
                return week;
            }

            @Override
            public int getDay() {
                return day;
            }

            @Override
            public int getSection() {
                return time;
            }

            @NotNull
            @Override
            public String getTeacher() {
                return techer;
            }
        };
        return lesson;
    }


    @Override
    public void notifyAutomatically() {

    }

    Lesson queryLesson(String[] selections) {
        return null;
    }

    /**
     * 该方法用于获取今天周几
     *
     * @return 一周的第几天
     */
    int getDay(Calendar calendar) {
        int day;
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        if (currentDay == Calendar.SUNDAY) {
            day = 7;
        } else {
            day = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return day;
    }

    /**
     * 该方法用于获取今天属于单周还是双周
     *
     * @param calendar
     * @return
     */
    int getWeek(Calendar calendar) {
        int week = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        int result = (week % 2 == 0) ? 2 : 1;
        return result;
    }

    /**
     * 该方法用于获取现在时间并将其转换为属于第几个Section
     *
     * @param calendar
     * @return
     */
    int getTime(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int curSection = (int) Math.ceil((hour * 60 + minute - 8 * 60 - 30) / 115);
        return curSection;
    }
}
