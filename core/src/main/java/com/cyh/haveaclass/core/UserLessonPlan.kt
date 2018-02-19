package com.cyh.haveaclass.core

interface UserLessonPlan : LessonPlan {
	
	override fun allLessons(): Collection<UserLesson>
	
	fun getLesson(id: Int): UserLesson
	
	fun addLesson(lesson: Lesson): Int
	
	fun addLessons(lessons: Collection<Lesson>): Collection<Int> {
		return lessons.map { addLesson(it) }
	}
	
	fun setLesson(id: Int, lesson: Lesson): Boolean
	
	fun removeLesson(id: Int): Boolean
	
	override fun selectLessonsByWeek(week: Int): Collection<UserLesson> {
		return allLessons().filter { it.week == week }
	}
	
	override fun selectLessonsByDay(week: Int, day: Int): Collection<UserLesson> {
		return allLessons().filter { it.week == week && it.dayOfWeek == day }
	}
	
	override fun selectLessonsBySection(week: Int, day: Int, section: Int): Collection<UserLesson> {
		return allLessons().filter { it.week == week && it.dayOfWeek == day && section == section }
	}
	
}

interface UserLesson : Lesson {
	/**
	 * 课的id
	 * 在同一个[UserLessonPlan]中区分各个[UserLesson]
	 */
	val id: Int
}

data class InstantUserLesson(
	override val id: Int,
	override val name: String,
	override val type: String,
	override val week: Int,
	override val dayOfWeek: Int,
	override val section: Int,
	override val place: String,
	override val teacher: String
) : UserLesson
