package com.cyh.haveaclass.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.cyh.haveaclass.R
import com.cyh.haveaclass.core.Lesson
import com.cyh.haveaclass.core.PlanUtils
import com.cyh.haveaclass.core.WebSitePlan
import com.cyh.haveaclass.utils.tryOrNull
import kotlinx.android.synthetic.main.adapter_day.view.*
import kotlinx.android.synthetic.main.adapter_lesson.view.*
import kotlinx.android.synthetic.main.fragment_lesson_list.*
import kotlinx.coroutines.experimental.CommonPool
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
		
		lessonListView.layoutManager = LinearLayoutManager(context)
		lessonListView.adapter = DayListAdapter()
		
		swipeRefreshLayout.setOnRefreshListener { refreshLessonList() }
		
		refreshLessonList()
		
	}
	
	
	//list
	
	class DayListAdapter : Adapter<DayListAdapter.DayHolder>() {
		
		//data
		
		var dayPlans: List<DayPlan> = emptyList()
		
		override fun getItemCount(): Int = dayPlans.size
		
		
		//views
		
		class DayHolder(view: View) : ViewHolder(view) {
			val dayLayout = view.dayLayout
			val dayTitle: TextView = view.dayTitle
			val lessonList: RecyclerView = view.lessonList
			var showingLessons: List<Lesson> = emptyList()
			
			init {
				lessonList.layoutManager = LinearLayoutManager(itemView.context)
				lessonList.adapter = LessonListAdapter()
			}
			
		}
		
		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
			return LayoutInflater.from(parent.context)
				.inflate(R.layout.adapter_day, parent, false)
				.let { DayHolder(it) }
		}
		
		override fun onBindViewHolder(holder: DayHolder, position: Int) {
			val dayPlan = dayPlans[position]
			holder.run {
				dayLayout.setBackgroundColor(dayOfWeekToColor(dayPlan.dayOfWeek))
				dayTitle.text = PlanUtils.dayOfWeekToText(dayPlan.dayOfWeek)
				showingLessons = dayPlan.lessons
				lessonList.adapter.notifyDataSetChanged()
			}
		}
		
		private fun dayOfWeekToColor(dayOfWeek: Int): Int {
			return when (dayOfWeek) {
				1 -> Color.rgb(255, 127, 191)
				2 -> Color.rgb(255, 191, 127)
				3 -> Color.rgb(255, 255, 127)
				4 -> Color.rgb(191, 255, 127)
				5 -> Color.rgb(127, 255, 191)
				6 -> Color.rgb(127, 191, 255)
				7 -> Color.rgb(191, 127, 255)
				else -> Color.argb(0, 0, 0, 0)
			}
		}
		
	}
	
	class LessonListAdapter : Adapter<LessonListAdapter.LessonHolder>() {
		
		//data
		
		var lessons: List<Lesson> = emptyList()
		
		override fun getItemCount(): Int = lessons.size
		
		
		//views
		
		class LessonHolder(view: View) : ViewHolder(view) {
			val timeLabel: TextView = view.timeLabel
			val placeLabel: TextView = view.placeLabel
			val nameLabel: TextView = view.nameLabel
			val teacherLabel: TextView = view.teacherLabel
		}
		
		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonHolder {
			return LayoutInflater.from(parent.context)
				.inflate(R.layout.adapter_lesson, parent, false)
				.let { LessonHolder(it) }
		}
		
		override fun onBindViewHolder(holder: LessonHolder, position: Int) {
			val lesson = lessons[position]
			holder.run {
				timeLabel.text = PlanUtils.sectionOfDayToText(lesson.section.sectionOfDay)
				placeLabel.text = lesson.place
				nameLabel.text = lesson.run { "$name $type" }
				teacherLabel.text = lesson.teacher
				Unit
			}
		}
		
	}
	
	data class DayPlan(val weekType: Int, val dayOfWeek: Int, val lessons: List<Lesson>)
	
	private fun refreshLessonList() {
		swipeRefreshLayout.isRefreshing = true
		launch(CommonPool) {
			val allLessons = tryOrNull { webSitePlan.allLessons() }
			if (allLessons == null) {
				launch(UI) {
					Toast.makeText(getContext(), "获取课表信息失败！", Toast.LENGTH_SHORT).show()
					swipeRefreshLayout.isRefreshing = false
				}
				return@launch
			}
			
			val calendar = Calendar.getInstance()
			val nowSection = PlanUtils.nowSection(calendar)
			
			val todayLessons = allLessons.filter {
				it.section.weekType == nowSection.weekType
					&& it.section.dayOfWeek == nowSection.dayOfWeek
			}
			val today = DayPlan(nowSection.weekType, nowSection.dayOfWeek, todayLessons)
			
			val tomorrowLessons = allLessons.filter {
				it.section.weekType == nowSection.weekType
					&& it.section.dayOfWeek == nowSection.dayOfWeek + 1
			}
			val tomorrow = DayPlan(nowSection.weekType, nowSection.dayOfWeek + 1, tomorrowLessons)
			
			val showingDayPlans = listOf(today, tomorrow)
			launch(UI) {
				(lessonListView.adapter as DayListAdapter).run {
					dayPlans = showingDayPlans
					notifyDataSetChanged()
				}
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
