package com.cyh.haveaclass.ui

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import com.cyh.haveaclass.R
import com.cyh.haveaclass.core.Lesson
import com.cyh.haveaclass.core.PlanUtils
import kotlinx.android.synthetic.main.cv_lesson.view.*

class LessonView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        LayoutInflater.from(this.context).inflate(R.layout.cv_lesson, this, true)
    }

    var lesson: Lesson? = null
        set(value) {
            field = value
            timeLabel.text = PlanUtils.sectionOfDayToText(value?.section?.sectionOfDay ?: -1)
            placeLabel.text = value?.place ?: ""
            nameLabel.text = value?.run {
                val s = "$name $type"
                s
            } ?: ""
            teacherLabel.text = value?.teacher ?: ""
            dayLabel.text = PlanUtils.dayOfWeekToText(value?.section?.dayOfWeek ?: -1)

            val color = (value?.name ?: "").hashCode() % 0x888888 + 0x888888
            mainLayout.setBackgroundColor(color)
        }

}