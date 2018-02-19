package com.cyh.haveaclass.core


/**
 * 课表。包含若干节课
 */
interface Plan {
	
	/**
	 * 获取该[Plan]中的所有课
	 */
	fun allLessons(): Collection<Lesson>
	
	/**
	 * 根据[Lesson.weekType]筛选课
	 */
	fun selectLessonsByWeek(weekType: Int): Collection<Lesson> {
		return allLessons().filter { it.weekType == weekType }
	}
	
	/**
	 * 根据[Lesson.weekType]和[Lesson.dayOfWeek]筛选课
	 */
	fun selectLessonsByDay(weekType: Int, dayOfWeek: Int): Collection<Lesson> {
		return selectLessonsByWeek(weekType).filter { it.dayOfWeek == dayOfWeek }
	}
	
	/**
	 * 根据[Lesson.weekType]、[Lesson.dayOfWeek]和[Lesson.section]筛选课
	 */
	fun selectLessonsBySection(weekType: Int, dayOfWeek: Int, section: Int): Collection<Lesson> {
		return selectLessonsByDay(weekType, dayOfWeek).filter { it.section == section }
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
	 * 单周还是双周，
	 * 双周用 0 表示，单周用 1 表示，
	 * 周日视为一周的开始。
	 */
	val weekType: Int
	
	/**
	 * 课在星期几，
	 * 星期日用 0 表示，星期一到星期六用 1~6 表示
	 */
	val dayOfWeek: Int
	
	/**
	 * 课在哪一节，
	 * 第一节课从 1 开始，第七节课用 7 表示
	 */
	val section: Int
	
	/**
	 * 课的地点
	 */
	val place: String
	
	/**
	 * 授课老师名字
	 */
	val teacher: String
}

data class InstantLesson(
	override val name: String,
	override val type: String,
	override val weekType: Int,
	override val dayOfWeek: Int,
	override val section: Int,
	override val place: String,
	override val teacher: String
) : Lesson


