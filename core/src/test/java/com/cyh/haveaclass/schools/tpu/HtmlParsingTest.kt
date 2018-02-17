package com.cyh.haveaclass.schools.tpu

import org.junit.Test


class HtmlParsingTest {
	@Test
	fun run() {
		val lessons = TPULessonPlan("150Б52").allLessons()
		println(lessons.firstOrNull { it.week == 1 && it.day == 1 && it.section == 1 })
	}
	
	
}
