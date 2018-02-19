package com.cyh.haveaclass.core


interface LessonPlan {
	
	fun allLessons(): Collection<Lesson>
	
	fun selectLessonsByWeek(week: Int): Collection<Lesson> {
		return allLessons().filter { it.week == week }
	}
	
	fun selectLessonsByDay(week: Int, day: Int): Collection<Lesson> {
		return allLessons().filter { it.week == week && it.dayOfWeek == day }
	}
	
	fun selectLessonsBySection(week: Int, day: Int, section: Int): Collection<Lesson> {
		return allLessons().filter { it.week == week && it.dayOfWeek == day && section == section }
	}
	
}
