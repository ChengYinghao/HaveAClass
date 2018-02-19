package com.cyh.haveaclass.core

import org.junit.Test


class WebSitePlanUnitTest {
	@Test
	fun htmlParsing() {
		val lessons = WebSitePlan("150Б52").allLessons()
		println(lessons.firstOrNull { it.weekType == 1 && it.dayOfWeek == 1 && it.section == 1 })
	}
}
