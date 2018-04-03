package com.cyh.haveaclass.core

import java.util.*

object PlanUtils {
	
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
		if (nowMinuteOfDay > dayEnd) return 8
		return (nowMinuteOfDay - dayStart) / 115 + 1
	}
	
	fun nowSection2(calendar: Calendar = Calendar.getInstance()): Section {
		val weekType = calendar.run {
			val day = timeInMillis / 1000 / 3600 / 24 - 3
			val weekUTC = day / 7L + 1
			(weekUTC % 2).toInt()
		}
		val dayOfWeek = calendar.run {
			this@run.get(Calendar.DAY_OF_WEEK) - 1
		}
		val sectionOfDay = calendar.run {
			fun minuteOfDay(hour: Int, minute: Int) = hour * 60 + minute
			
			val nowMinuteOfDay = minuteOfDay(this@run.get(Calendar.HOUR_OF_DAY), this@run.get(Calendar.MINUTE))
			val dayStart = minuteOfDay(8, 30)
			val dayEnd = minuteOfDay(21, 35)
			
			when {
				nowMinuteOfDay < dayStart -> 0
				nowMinuteOfDay > dayEnd -> 8
				else -> (nowMinuteOfDay - dayStart) / 115 + 1
			}
		}
		return Section(weekType, dayOfWeek, sectionOfDay)
	}
	
	fun sectionOfDayToText(sectionOfDay: Int): String {
		return when (sectionOfDay) {
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
	
	fun textToSectionOfDay(time: String): Int {
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
