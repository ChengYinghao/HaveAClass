package com.cyh.haveaclass.schools.tpu

import com.cyh.haveaclass.core.Lesson
import com.cyh.haveaclass.core.LessonPlan
import java.net.URL

class TPULessonPlan : LessonPlan {
	
	fun url(groupName: String): String {
		return "http://rasp.tpu.ru/view.php?for=$groupName&aslist=1&weekType=1"
	}
	
	fun html(url: String): String {
		val connection = URL(url).openConnection()
		return connection.getInputStream().bufferedReader().readText()
	}
	
	override fun allLessons(): Collection<Lesson> {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}