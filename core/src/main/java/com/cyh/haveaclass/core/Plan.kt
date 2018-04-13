package com.cyh.haveaclass.core


/**
 * 课表。包含若干节课
 */
interface Plan {
	
	/**
	 * 获取该[Plan]中的所有课
	 */
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
	 * 课的时间
	 */
	val section: Section
	
	/**
	 * 课的地点
	 */
	val place: String
	
	/**
	 * 授课老师名字
	 */
	val teacher: String
}
