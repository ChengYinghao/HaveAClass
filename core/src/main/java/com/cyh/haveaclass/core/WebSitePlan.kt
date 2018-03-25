package com.cyh.haveaclass.core

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class WebSitePlan(val groupName: String) : Plan {
	
	override fun allLessons(): Collection<Lesson> {
		return lessonCache ?: fetchLessons()
	}
	
	private var lessonCache: List<Lesson>? = null
	
	fun fetchLessons(): List<InstantLesson> {
		val url = "http://rasp.tpu.ru/view.php?for=$groupName&aslist=1&weekType=1"
		val doc = Jsoup.connect(url).get()
		return parsePageHtml(doc).also { lessonCache = it }
	}
	
	fun clearCache() {
		lessonCache = null
		System.gc()
	}
	
	private fun parsePageHtml(pageHtml: Document): List<InstantLesson> {
		val div_week1 = pageHtml.select("#j_weekListOddBefore1").first()
		val classes1 = parseWeekHtml(div_week1, 1)
		
		val div_week2 = pageHtml.select("#j_weekListOddBefore2").first()
		val classes2 = parseWeekHtml(div_week2, 0)
		
		return classes1 + classes2
	}
	
	private fun parseWeekHtml(weekHtml: Element, weekType: Int): List<InstantLesson> {
		return weekHtml.child(1).child(0).children().flatMap { li_week ->
			val dayOfWeek = PlanUtils.textToDayOfWeek(li_week.child(0).text())
			li_week.child(1).children().map { li_lesson ->
				val section = PlanUtils.textToSection(li_lesson.child(0).text())
				InstantLesson(
					name = li_lesson.child(1).text(),
					type = li_lesson.child(2).text(),
					weekType = weekType,
					dayOfWeek = dayOfWeek,
					section = section,
					place = li_lesson.child(3).text(),
					teacher = li_lesson.child(4).text()
				)
			}
		}
	}
	
	
}
