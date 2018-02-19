package com.cyh.haveaclass.core

import java.util.*

object LessonPlanUtils {
	
	fun nowDayOfWeek(calendar: Calendar = Calendar.getInstance()): Int {
		return calendar.get(Calendar.DAY_OF_WEEK) - 1
	}
	
	fun nowWeekType(calendar: Calendar = Calendar.getInstance()): Int {
		val day = calendar.timeInMillis / 1000 / 3600 / 24 - 3
		val weekUTC = day / 7L + 1
		return (weekUTC % 2).toInt()
	}
	
	fun nowSection(calendar: Calendar = Calendar.getInstance()): Int {
		fun minuteOfDay(hour: Int, minute: Int) = hour * 60 + minute
		
		val nowMinuteOfDay = minuteOfDay(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
		val dayStart = minuteOfDay(8, 30)
		val dayEnd = minuteOfDay(21, 35)
		
		if (nowMinuteOfDay < dayStart) return 0
		if (nowMinuteOfDay > dayEnd) return 0
		return (nowMinuteOfDay - dayStart) / 115 + 1
	}
	
}