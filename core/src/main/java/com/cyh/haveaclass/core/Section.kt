package com.cyh.haveaclass.core

data class Week(
	val weekType: Int
)

fun Week.next(): Week {
	return Week((weekType + 1) % 2)
}

fun Week.last(): Week {
	return next()
}

operator fun Week.unaryPlus(): Week = next()

operator fun Week.unaryMinus(): Week = last()


data class Day(
	val weekType: Int,
	val dayOfWeek: Int
)

fun Day.next(): Day {
	var nextDayOfWeek = dayOfWeek
	var nextWeekType = weekType
	
	kotlin.run {
		nextDayOfWeek++
		if (nextDayOfWeek <= 6) return@run
		nextDayOfWeek = 0
		
		nextWeekType++
		if (nextWeekType <= 1) return@run
		nextWeekType = 0
	}
	
	return Day(nextWeekType, nextDayOfWeek)
}

fun Day.last(): Day {
	var lastDayOfWeek = dayOfWeek
	var lastWeekType = weekType
	
	kotlin.run {
		lastDayOfWeek--
		if (lastDayOfWeek >= 0) return@run
		lastDayOfWeek = 6
		
		lastWeekType--
		if (lastWeekType >= 0) return@run
		lastWeekType = 1
	}
	
	return Day(lastWeekType, lastDayOfWeek)
}

operator fun Day.unaryPlus(): Day = next()

operator fun Day.unaryMinus(): Day = last()

fun Day.week(): Week = Week(weekType)


data class Section(
	/**
	 * 单周还是双周（周日视为一周的开始）。
	 * @return 双周用 0 表示，单周用 1 表示。
	 */
	val weekType: Int,
	/**
	 * 星期几
	 * @return 星期日用 0 表示，星期一到星期六用 1~6 表示
	 */
	val dayOfWeek: Int,
	/**
	 * 哪一节课，若在课间则当作是前一节课。
	 * @return
	 * 第一到第七节课用 1~7 表示，
	 * 若在早上上课前则用 0 表示，
	 * 若在晚上放学后则用 8 表示。
	 */
	val sectionOfDay: Int
)

fun Section.next(): Section {
	var nextSection = sectionOfDay
	var nextDayOfWeek = dayOfWeek
	var nextWeekType = weekType
	
	kotlin.run {
		nextSection++
		if (nextSection <= 7) return@run
		nextSection = 1
		
		nextDayOfWeek++
		if (nextDayOfWeek <= 6) return@run
		nextDayOfWeek = 0
		
		nextWeekType++
		if (nextWeekType <= 1) return@run
		nextWeekType = 0
	}
	
	return Section(nextWeekType, nextDayOfWeek, nextSection)
}

fun Section.last(): Section {
	var lastSection = sectionOfDay
	var lastDayOfWeek = dayOfWeek
	var lastWeekType = weekType
	
	kotlin.run {
		lastSection--
		if (lastSection >= 1) return@run
		lastSection = 7
		
		lastDayOfWeek--
		if (lastDayOfWeek >= 0) return@run
		lastDayOfWeek = 6
		
		lastWeekType--
		if (lastWeekType >= 0) return@run
		lastWeekType = 1
	}
	
	return Section(lastWeekType, lastDayOfWeek, lastSection)
}

operator fun Section.unaryPlus(): Section = next()

operator fun Section.unaryMinus(): Section = last()

fun Section.day(): Day = Day(weekType, dayOfWeek)

fun Section.week(): Week = Week(weekType)