package com.cyh.haveaclass.core

import org.junit.Test


class WebSitePlanUnitTest {
	@Test
	fun htmlParsing() {
		val plan = WebSitePlan("150Б52")
		val lessons = plan.selectLessonsBySection(0, 2, 3)
		lessons.forEach { println(it) }
	}
}
