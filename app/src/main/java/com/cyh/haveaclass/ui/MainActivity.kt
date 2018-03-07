package com.cyh.haveaclass.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.cyh.haveaclass.R
import com.cyh.haveaclass.core.Lesson
import com.cyh.haveaclass.core.PlanUtils
import com.cyh.haveaclass.core.WebSitePlan
import java.util.*


class MainActivity : AppCompatActivity() {
    internal var webSitePlan: WebSitePlan? = null
    internal var button: Button? = null
    internal var display: TextView? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webSitePlan = WebSitePlan("150Б52")
        button = findViewById(R.id.nextLesson_display_button)
        display = findViewById(R.id.nextLesson_display_text)
        button?.setOnClickListener {
            Thread(Runnable {
                val calendar = Calendar.getInstance()
                val day = PlanUtils.nowDayOfWeek(calendar)
                val week = PlanUtils.nowWeekType(calendar)
                val section = PlanUtils.nowSection(calendar)
                val nextSection = section + 1
                if (nextSection >= 8) {
                    display?.post { display?.text = "今天没课啦，玩去吧！" }
                    return@Runnable
                }
                val nextLessons = webSitePlan?.selectLessonsBySection(week, day, nextSection)
                val size = nextLessons?.size
                if (size == 0) {
                    display?.post { display?.text = "下节没课，开心吧" }
                } else {
                    val nextLesson = StringBuilder()
                    if (nextLessons != null) {
                        for (lesson in nextLessons) {
                            nextLesson.insert(0, displayLesson(lesson) + ";")
                        }
                    }
                    val finalNextLesson = nextLesson.toString()
                    display?.post { display?.text = finalNextLesson }

                }
            }).start()
        }
    }

    internal fun displayLesson(lesson: Lesson): String {
        val place = lesson.place
        val name = lesson.name
        val teacher = lesson.teacher
        val section = lesson.section
        val type = lesson.type
        val time: String
        when (section) {
            1 -> time = "8:30"
            2 -> time = "10:25"
            3 -> time = "12:20"
            4 -> time = "14:15"
            5 -> time = "16:10"
            6 -> time = "18:05"
            else -> time = "休息时间"
        }
        return "下节课是" + name + "的" + type + ", " + time + "在" + place + "上课, " + "老师是" + teacher
    }


}
