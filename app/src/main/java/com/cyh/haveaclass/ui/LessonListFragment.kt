package com.cyh.haveaclass.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.cyh.haveaclass.R
import com.cyh.haveaclass.core.Lesson
import com.cyh.haveaclass.core.PlanUtils
import com.cyh.haveaclass.core.WebSitePlan
import kotlinx.android.synthetic.main.adapter_day.view.*
import kotlinx.android.synthetic.main.adapter_lesson.view.*
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
		
		lessonListView.layoutManager = LinearLayoutManager(context)
		lessonListView.adapter = object : RecyclerView.Adapter<DayHolder>() {
			override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DayHolder {
				return DayHolder(LayoutInflater.from(context).inflate(R.layout.adapter_day, parent, false))
			}
			
			override fun getItemCount(): Int = showingDays.size
			
			override fun onBindViewHolder(holder: DayHolder, position: Int) {
				val day = showingDays[position]
				holder.update(day)
			}
			
		}
		
		swipeRefreshLayout.setOnRefreshListener { refreshLessonList() }
		
		refreshLessonList()
		
	}
	
	
	//lessonList
	private var showingDays: List<Day> = emptyList()
	
	private fun refreshLessonList() {
		swipeRefreshLayout.isRefreshing = true
		launch {
			try {
				webSitePlan.loadLessons()
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
				val todayLessons = allLessons.filter {
					it.section.weekType == nowSection.weekType
						&& it.section.dayOfWeek == nowSection.dayOfWeek
				}
				val today = Day(nowSection.weekType, nowSection.dayOfWeek, todayLessons)
				
				val tomorrowLessons = allLessons.filter {
					it.section.weekType == nowSection.weekType
						&& it.section.dayOfWeek == nowSection.dayOfWeek + 1
				}
				val tomorrow = Day(nowSection.weekType, nowSection.dayOfWeek + 1, tomorrowLessons)
				
				showingDays = listOf(today, tomorrow)
				lessonListView.adapter.notifyDataSetChanged()
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

class DayHolder(view: View) : RecyclerView.ViewHolder(view) {
	private val dayLabel = view.dayLabel
	private val lessonList = view.lessonList
	
	private var showingLessons: List<Lesson> = emptyList()
	
	init {
		lessonList.layoutManager = LinearLayoutManager(itemView.context)
		lessonList.adapter = object : RecyclerView.Adapter<LessonHolder>() {
			
			override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LessonHolder {
				return LessonHolder(LessonView(this@DayHolder.itemView.context))
			}
			
			override fun getItemCount(): Int = showingLessons.size
			
			override fun onBindViewHolder(holder: LessonHolder, position: Int) {
				holder.update(showingLessons[position])
			}
			
		}
	}
	
	fun update(day: Day) {
		dayLabel.text = PlanUtils.dayOfWeekToText(day.dayOfWeek)
		showingLessons = day.lessons
		lessonList.adapter.notifyDataSetChanged()
	}
}

data class Day(val weekType: Int, val dayOfWeek: Int, val lessons: List<Lesson>)

class LessonHolder(view: LessonView) : RecyclerView.ViewHolder(view) {
	private val timeLabel = view.timeLabel
	private val placeLabel = view.placeLabel
	private val nameLabel = view.nameLabel
	private val teacherLabel = view.teacherLabel
	fun update(lesson: Lesson) {
		timeLabel.text = PlanUtils.sectionOfDayToText(lesson.section.sectionOfDay)
		placeLabel.text = lesson.place
		nameLabel.text = lesson.run { "$name $type" }
		teacherLabel.text = lesson.teacher
	}
}
