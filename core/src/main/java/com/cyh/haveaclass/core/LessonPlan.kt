package com.cyh.haveaclass.core


interface LessonPlan {
	fun allLessons(): Collection<Lesson>
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
	 * 1 表示单周，2表示双周
	 */
	val week: Int
	
	/**
	 * 课在星期几
	 * （0表示星期日）
	 */
	val day: Int
	
	/**
	 * 课在哪一节
	 */
	val section: Int
	
	/**
	 * 授课老师名字
	 */
	val teacher: String
}
