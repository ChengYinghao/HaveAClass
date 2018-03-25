package com.cyh.haveaclass.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.cyh.haveaclass.R
import com.cyh.haveaclass.core.Lesson
import com.cyh.haveaclass.core.PlanUtils
import com.cyh.haveaclass.core.WebSitePlan
import kotlinx.android.synthetic.main.fragment_lesson_list.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
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
	private val lessonList = ArrayList<Lesson>()
	
	private fun refreshLessonList() {
		swipeRefreshLayout.isRefreshing = true
		launch {
			webSitePlan.fetchLessons()
			launch(UI) {
				val calendar = Calendar.getInstance()
				val nowWeekType = PlanUtils.nowWeekType(calendar)
				val nowDayOfWeek = PlanUtils.nowDayOfWeek(calendar)
				val nowSection = PlanUtils.nowSection(calendar)
				
				lessonList.clear()
				
				lessonList += webSitePlan.selectLessonsBySection(nowWeekType, nowDayOfWeek, nowSection)
				
				if (nowSection < 8)
					lessonList += webSitePlan.selectLessonsBySection(nowWeekType, nowDayOfWeek, nowSection + 1)
				
				if (nowSection > 7)
					lessonList += webSitePlan.selectLessonsByDay(nowWeekType, nowDayOfWeek + 1)
				
				(lessonListView.adapter as BaseAdapter).notifyDataSetChanged()
				swipeRefreshLayout.isRefreshing = false
			}
		}
	}
	
}
