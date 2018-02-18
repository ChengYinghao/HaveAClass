package com.cyh.haveaclass.core

import java.util.*

object LessonPlanUtils {
	
	fun getNowDay(calendar: Calendar = Calendar.getInstance()): Int {
		return calendar.get(Calendar.DAY_OF_WEEK).let { day ->
			if (day == Calendar.SUNDAY) 7
			else day - 1
		}
	}
	
	fun getNowWeek(calendar: Calendar = Calendar.getInstance()): Int {
		val dayUTC = calendar.timeInMillis / 1000 / 3600 / 24 - 4
		return ((dayUTC / 7L) % 2 + 1).toInt()
	}
	
	fun getSection(calendar: Calendar): Int {
		val hour = calendar.get(Calendar.HOUR_OF_DAY)
		val minute = calendar.get(Calendar.MINUTE)
		return Math.ceil(((hour * 60 + minute - 8 * 60 - 30) / 115).toDouble()).toInt()
	}
	
}