package com.cyh.haveaclass.core


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