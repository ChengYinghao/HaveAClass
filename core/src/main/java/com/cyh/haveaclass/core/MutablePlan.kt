package com.cyh.haveaclass.core


interface MutablePlan : Plan {
	
	override fun allLessons(): Collection<MutableLesson>
	
	fun getLesson(id: Int): MutableLesson
	
	fun addLesson(lesson: Lesson): Int
	
	fun addLessons(lessons: Collection<Lesson>): Collection<Int> {
		return lessons.map { addLesson(it) }
	}
	
	fun setLesson(id: Int, lesson: Lesson): Boolean
	
	fun removeLesson(id: Int): Boolean
	
	override fun selectLessonsByWeek(week: Int): Collection<MutableLesson> {
		return allLessons().filter { it.weekType == week }
	}
	
	override fun selectLessonsByDay(week: Int, day: Int): Collection<MutableLesson> {
		return selectLessonsByWeek(week).filter { it.dayOfWeek == day }
	}
	
	override fun selectLessonsBySection(week: Int, day: Int, section: Int): Collection<MutableLesson> {
		return selectLessonsByDay(week, day).filter { it.section == section }
	}
	
}

interface MutableLesson : Lesson {
	/**
	 * 课的id
	 * 在同一个[MutablePlan]中区分各个[MutableLesson]的标识
	 */
	val id: Int
}

data class InstantMutableLesson(
	override val id: Int,
	override val name: String,
	override val type: String,
	override val weekType: Int,
	override val dayOfWeek: Int,
	override val section: Int,
	override val place: String,
	override val teacher: String
) : MutableLesson
