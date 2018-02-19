package com.cyh.haveaclass.core

import java.util.*

object LessonPlanUtils {
	
	/**
	 * @return 现在是星期几，
	 * 星期日用 0 表示，星期一到星期六用 1~6 表示
	 */
	fun nowDayOfWeek(calendar: Calendar = Calendar.getInstance()): Int {
		return calendar.get(Calendar.DAY_OF_WEEK) - 1
	}
	
	/**
	 * @return 当前是单周还是双周。
	 * 双周用 0 表示，单周用 1 表示。
	 * 周日视为一周的开始。
	 */
	fun nowWeekType(calendar: Calendar = Calendar.getInstance()): Int {
		val day = calendar.timeInMillis / 1000 / 3600 / 24 - 3
		val weekUTC = day / 7L + 1
		return (weekUTC % 2).toInt()
	}
	
	/**
	 * @return 现在在哪一节课，
	 * 第一到第七节课用 1~7 表示。
	 * 若在早上上课前则用 0 表示。
	 * 若在晚上放学后则用 8 表示。
	 * 若处于课间则当作是前一节课。
	 */
	fun nowSection(calendar: Calendar = Calendar.getInstance()): Int {
		fun minuteOfDay(hour: Int, minute: Int) = hour * 60 + minute
		
		val nowMinuteOfDay = minuteOfDay(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
		val dayStart = minuteOfDay(8, 30)
		val dayEnd = minuteOfDay(21, 35)
		
		if (nowMinuteOfDay < dayStart) return 0
		if (nowMinuteOfDay > dayEnd) return 8
		return (nowMinuteOfDay - dayStart) / 115 + 1
	}
	
}