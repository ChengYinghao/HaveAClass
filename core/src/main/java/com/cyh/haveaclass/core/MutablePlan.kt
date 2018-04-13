package com.cyh.haveaclass.core


/**
 * 可修改的课表。
 * 为了方便修改时指定，课会被加上[MutableLesson.id]属性
 */
interface MutablePlan : Plan {
	
	override fun allLessons(): Collection<MutableLesson>
	
	/**
	 * 根据id获取对应的那节课
	 * @param id 课的id
	 * @return 根据[id]获取对应的课
	 */
	fun getLesson(id: Int): MutableLesson
	
	/**
	 * 添加一节课
	 * @param lesson 加的一节课
	 * @return 新加入的课分配到的id
	 */
	fun addLesson(lesson: Lesson): Int
	
	/**
	 * 批量地添加一堆课
	 * @param lessons 批量地添加的一堆课
	 */
	fun addLessons(lessons: Collection<Lesson>) {
		lessons.forEach { addLesson(it) }
	}
	
	/**
	 * 根据id修改那节课的信息
	 * @param id 课的id
	 */
	fun setLesson(id: Int, lesson: Lesson)
	
	/**
	 * 根据id删除对应的那节课
	 * @param id 课的id
	 */
	fun removeLesson(id: Int)
	
}

interface MutableLesson : Lesson {
	/**
	 * 课的id
	 * 在同一个[MutablePlan]中区分各个[MutableLesson]的标识
	 */
	val id: Int
	override var name: String
	override var type: String
	override var section: Section
	override var place: String
	override var teacher: String
}
