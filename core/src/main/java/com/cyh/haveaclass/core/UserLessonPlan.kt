package com.cyh.haveaclass.core

abstract class UserLessonPlan : LessonPlan {
	
	abstract override fun allLessons(): Collection<UserLesson>
	
	abstract fun getLesson(id: Int): UserLesson
	
	abstract fun addLesson(lesson: Lesson): Int
	
	abstract fun setLesson(id: Int, lesson: Lesson): Boolean
	
	abstract fun removeLesson(id: Int)
	
	open fun selectLessonsByTime(week: Int, day: Int, section: Int): Collection<UserLesson> {
		return allLessons().filter { it.week == week && it.day == day && section == section }
	}
	
}

data class UserLesson(
	/**
	 * 课的id
	 * 在同一个[UserLessonPlan]中区分各个[UserLesson]
	 */
	val id: Int,
	override val name: String,
	override val type: String,
	override val place: String,
	override val week: Int,
	override val day: Int,
	override val section: Int,
	override val teacher: String
) : Lesson
