package com.cyh.haveaclass.core

import org.junit.Assert
import org.junit.Test
import java.util.*

class UtilsUnitTest {
	
	@Test
	fun nowWeekType() {
		val calendar = Calendar.getInstance()
		
		val dates = intArrayOf(17, 18, 19)
		val weekTypes = dates.map { date ->
			calendar.set(2018, 2, date)
			LessonPlanUtils.nowWeekType(calendar)
		}.toIntArray()
		val rightAnswer = intArrayOf(1, 0, 0)
		
		Assert.assertArrayEquals(rightAnswer, weekTypes)
	}
	
	@Test
	fun nowDayOfWeek() {
		val calendar = Calendar.getInstance()
		
		val dates = intArrayOf(17, 18, 19)
		val weekTypes = dates.map { date ->
			calendar.set(2018, 2, date)
			LessonPlanUtils.nowDayOfWeek(calendar)
		}.toIntArray()
		val rightAnswer = intArrayOf(6, 0, 1)
		
		Assert.assertArrayEquals(rightAnswer, weekTypes)
	}
	
	@Test
	fun nowSection() {
		val calendar = Calendar.getInstance()
		
		val time = arrayOf(4 to 0, 8 to 31, 10 to 24, 10 to 26, 21 to 34, 21 to 36)
		val sections = time.map { (hour, minute) ->
			calendar.set(Calendar.HOUR_OF_DAY, hour)
			calendar.set(Calendar.MINUTE, minute)
			LessonPlanUtils.nowSection(calendar)
		}.toIntArray()
		val rightAnswer = intArrayOf(0, 1, 1, 2, 7, 8)
		
		Assert.assertArrayEquals(rightAnswer, sections)
	}
	
}