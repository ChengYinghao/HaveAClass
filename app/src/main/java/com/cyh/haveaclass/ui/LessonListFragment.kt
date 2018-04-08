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
import java.util.*

class LessonListFragment : Fragment() {
	
	companion object {
		private const val argName_groupName = "groupName"
		var LessonListFragment.argGroupName: String
			get() = arguments.getString(argName_groupName)
			set(value) {
				arguments = (arguments ?: Bundle()).apply { putString(argName_groupName, value) }
			}
		
		fun newInstance(groupName: String): LessonListFragment {
			return LessonListFragment().apply { argGroupName = groupName }
		}
	}
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		return inflater.inflate(R.layout.fragment_lesson_list, container, false)
	}
	
	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		
		searchButton.setOnClickListener {
			fragmentManager.beginTransaction()
				.replace(R.id.fragmentContainer, SearchLessonFragment.newInstance(argGroupName))
				.addToBackStack(null)
				.commit()
		}
		
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
	private var lessonList: List<Lesson> = emptyList()
	
	private fun refreshLessonList() {
		swipeRefreshLayout.isRefreshing = true
		launch {
			try {
				webSitePlan.fetchLessons()
			} catch (e: Exception) {
				launch(UI) {
					Toast.makeText(getContext(), "获取课表信息失败！", Toast.LENGTH_SHORT).show()
					swipeRefreshLayout.isRefreshing = false
				}
				return@launch
			}
			launch(UI) {
				val calendar = Calendar.getInstance()
				val nowSection = PlanUtils.nowSection2(calendar)
				
				val allLessons = webSitePlan.allLessons()
				lessonList = allLessons.filter {
					if (it.section.weekType == nowSection.weekType) {
						if (it.section.dayOfWeek == nowSection.dayOfWeek) {
							it.section.sectionOfDay >= nowSection.sectionOfDay
						} else it.section.dayOfWeek == nowSection.dayOfWeek + 1
					} else false
				}
				(lessonListView.adapter as BaseAdapter).notifyDataSetChanged()
				swipeRefreshLayout.isRefreshing = false
			}
		}
	}
	
	
	//webSitePlan
	private lateinit var webSitePlan: WebSitePlan
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		webSitePlan = WebSitePlan(argGroupName)
	}
	
}
