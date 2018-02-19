package com.cyh.haveaclass.core

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element

class WebSitePlan(val groupName: String) : Plan {
	
	override fun allLessons(): Collection<InstantLesson> {
		val url = "http://rasp.tpu.ru/view.php?for=$groupName&aslist=1&weekType=1"
		val doc = Jsoup.connect(url).get()
		return parsePageHtml(doc)
	}
	
	private fun parsePageHtml(pageHtml: Document): List<InstantLesson> {
		val div_week1 = pageHtml.selectFirst("#j_weekListOddBefore1")
		val div_week2 = pageHtml.selectFirst("#j_weekListOddBefore2")
		
		val classes1 = parseWeekHtml(div_week1, 1)
		val classes2 = parseWeekHtml(div_week2, 2)
		return classes1 + classes2
	}
	
	private fun parseWeekHtml(weekHtml: Element, weekType: Int): List<InstantLesson> {
		return weekHtml.child(1).child(0).children().flatMap { li_week ->
			val dayOfWeek = parseDayText(li_week.child(0).text())
			li_week.child(1).children().map { li_lesson ->
				val section = parseSectionText(li_lesson.child(0).text())
				val instantLesson = InstantLesson(
					name = li_lesson.child(1).text(),
					type = li_lesson.child(2).text(),
					weekType = weekType,
					dayOfWeek = dayOfWeek,
					section = section,
					place = li_lesson.child(3).text(),
					teacher = li_lesson.child(4).text()
				)
				instantLesson
			}
		}
	}
	
	private fun parseDayText(dayText: String): Int {
		return when (dayText) {
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
	
	private fun parseSectionText(sectionText: String): Int {
		return when (sectionText) {
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
	
}