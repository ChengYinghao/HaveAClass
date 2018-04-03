package com.cyh.haveaclass.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.cyh.haveaclass.R
import com.cyh.haveaclass.core.Lesson
import com.cyh.haveaclass.core.PlanUtils
import com.cyh.haveaclass.core.WebSitePlan
import kotlinx.android.synthetic.main.fragment_lesson_list.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.io.IOException
import java.util.*

class LessonListFragment : Fragment() {
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return inflater.inflate(R.layout.fragment_lesson_list, container, false)
	}
	
	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		lessonListView.adapter = object : BaseAdapter() {
			override fun getCount(): Int = lessonList.size
			override fun getItemId(position: Int): Long = 0L
			override fun getItem(position: Int): Lesson = lessonList[position]
			override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
				val lessonView = (convertView as? LessonView) ?: LessonView(context)
				lessonView.lesson = getItem(position)
				return lessonView
			}
		}
		
		swipeRefreshLayout.setOnRefreshListener { refreshLessonList() }
		
		refreshLessonList()
		
	}
	
	//lessonList
	private val webSitePlan = WebSitePlan("150Б52")
	private var lessonList: List<Lesson> = emptyList()
	
	private fun refreshLessonList() {
		swipeRefreshLayout.isRefreshing = true
		launch {
			try {
				webSitePlan.fetchLessons()
			} catch (e: IOException) {
				launch(UI) {
					Toast.makeText(getContext(), "获取课表信息失败！", Toast.LENGTH_SHORT).show()
					swipeRefreshLayout.isRefreshing = false
				}
				return@launch
			}
			launch(UI) {
				val calendar = Calendar.getInstance()
				val nowSection = PlanUtils.nowSection2(calendar)
				
				lessonList = webSitePlan.allLessons().filter { lesson ->
					lesson.section.run {
						val conditionWeekType = (weekType - nowSection.weekType) == 0
						val conditionDayOfWeek = (dayOfWeek - nowSection.dayOfWeek) in 0..2
						val conditionSectionOfDay = (sectionOfDay - nowSection.sectionOfDay) >= 0
						conditionWeekType && (conditionDayOfWeek || conditionSectionOfDay)
					}
				}.toList()
				(lessonListView.adapter as BaseAdapter).notifyDataSetChanged()
				swipeRefreshLayout.isRefreshing = false
			}
		}
	}
	
}
