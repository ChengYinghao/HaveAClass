package com.cyh.haveaclass.core

import java.util.*

object PlanUtils {
	
	/**
	 * @return [calendar]的时间是星期几，
	 * 星期日用 0 表示，星期一到星期六用 1~6 表示
	 */
	fun nowDayOfWeek(calendar: Calendar = Calendar.getInstance()): Int {
		return calendar.get(Calendar.DAY_OF_WEEK) - 1
	}
	
	/**
	 * @return [calendar]的时间是单周还是双周。
	 * 双周用 0 表示，单周用 1 表示。
	 * 周日视为一周的开始。
	 */
	fun nowWeekType(calendar: Calendar = Calendar.getInstance()): Int {
		val day = calendar.timeInMillis / 1000 / 3600 / 24 - 3
		val weekUTC = day / 7L + 1
		return (weekUTC % 2).toInt()
	}
	
	/**
	 * @return [calendar]的时间在哪一节课，
	 * 第一到第七节课用 1~7 表示。
	 * 若在早上上课前则用 0 表示。
	 * 若在晚上放学后则用 8 表示。
	 * 若在课间则当作是前一节课。
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
	
	fun sectionToText(section: Int): String {
		return when (section) {
			1 -> "08:30"
			2 -> "10:25"
			3 -> "12:20"
			4 -> "14:15"
			5 -> "16:10"
			6 -> "18:05"
			7 -> "20:00"
			else -> ""
		}
	}
	
	fun textToSection(time: String): Int {
		return when (time) {
			"08:30" -> 1
			"10:25" -> 2
			"12:20" -> 3
			"14:15" -> 4
			"16:10" -> 5
			"18:05" -> 6
			"20:00" -> 7
			else -> -1
		}
	}
	
	fun dayOfWeekToText(dayOfWeek: Int): String {
		return when (dayOfWeek) {
			1 -> "Понедельник"
			2 -> "Вторник"
			3 -> "Среда"
			4 -> "Четверг"
			5 -> "Пятница"
			6 -> "Суббота"
			7 -> "Воскресение"
			else -> ""
		}
	}
	
	fun textToDayOfWeek(text: String): Int {
		return when (text) {
			"Понедельник" -> 1
			"Вторник" -> 2
			"Среда" -> 3
			"Четверг" -> 4
			"Пятница" -> 5
			"Суббота" -> 6
			"Воскресение" -> 7
			else -> -1
		}
	}
	
}
