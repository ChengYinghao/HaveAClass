package com.cyh.haveaclass.core


interface LessonPlan {
	
	fun allLessons(): Collection<Lesson>
	
	fun selectLessonsByWeek(week: Int): Collection<Lesson> {
		return allLessons().filter { it.weekType == week }
	}
	
	fun selectLessonsByDay(week: Int, day: Int): Collection<Lesson> {
		return selectLessonsByWeek(week).filter { it.dayOfWeek == day }
	}
	
	fun selectLessonsBySection(week: Int, day: Int, section: Int): Collection<Lesson> {
		return selectLessonsByDay(week, day).filter { it.section == section }
	}
	
}
