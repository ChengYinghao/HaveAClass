package com.cyh.haveaclass.core


interface LessonPlan {
	
	fun allLessons(): Collection<Lesson>
	
	fun selectLessonsByWeek(week: Int): Collection<Lesson> {
		return allLessons().filter { it.week == week }
	}
	
	fun selectLessonsByDay(week: Int, day: Int): Collection<Lesson> {
		return allLessons().filter { it.week == week && it.day == day }
	}
	
	fun selectLessonsBySection(week: Int, day: Int, section: Int): Collection<Lesson> {
		return allLessons().filter { it.week == week && it.day == day && section == section }
	}
	
}

interface Lesson {
	
	/**
	 * 课的名字
	 */
	val name: String
	
	/**
	 * 课的类型（ЛК, ЛБ, ПР）
	 */
	val type: String
	
	/**
	 * 课的地点
	 */
	val place: String
	
	/**
	 * 单周还是双周
	 * 1 表示单周，2 表示双周
	 */
	val week: Int
	
	/**
	 * 课在星期几
	 * （星期一是 1 ，星期日是 7 ）
	 */
	val day: Int
	
	/**
	 * 课在哪一节
	 * 第一节课从 1 开始
	 */
	val section: Int
	
	/**
	 * 授课老师名字
	 */
	val teacher: String
}

data class InstantLesson(
	override val name: String,
	override val type: String,
	override val place: String,
	override val week: Int,
	override val day: Int,
	override val section: Int,
	override val teacher: String
) : Lesson