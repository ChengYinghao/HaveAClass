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
		
		var offset: Int = 0
		
		override fun getItemCount(): Int = dayPlans.size
		
		
		//views
		
		class DayHolder(view: View) : ViewHolder(view) {
			val dayLayout: ViewGroup = view.dayLayout
			val dayTitle: TextView = view.dayTitle
			val lessonList: RecyclerView = view.lessonList
			
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
			val dayPlan = dayPlans[(position + offset) % dayPlans.size]
			holder.run {
				dayLayout.setBackgroundColor(dayOfWeekToColor(dayPlan.dayOfWeek))
				dayTitle.text = PlanUtils.dayOfWeekToText(dayPlan.dayOfWeek)
				(lessonList.adapter as LessonListAdapter).run {
					lessons = dayPlan.lessons
					notifyDataSetChanged()
				}
			}
		}
		
		private fun dayOfWeekToColor(dayOfWeek: Int): Int {
			return when (dayOfWeek) {
				0 -> Color.rgb(191, 223, 255)
				1 -> Color.rgb(223, 191, 255)
				2 -> Color.rgb(255, 191, 223)
				3 -> Color.rgb(255, 223, 191)
				4 -> Color.rgb(255, 255, 191)
				5 -> Color.rgb(223, 255, 191)
				6 -> Color.rgb(191, 255, 223)
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
			val typeLabel: TextView = view.typeLabel
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
				nameLabel.text = lesson.name
				typeLabel.text = lesson.type
				teacherLabel.text = lesson.teacher
			}
		}
		
	}
	
	data class DayPlan(val weekType: Int, val dayOfWeek: Int, val lessons: List<Lesson>)
	
	private fun arrangeLessons(allLessons: List<Lesson>): List<DayPlan> {
		return Array(14) { index ->
			val dayOfWeek = index % 7
			val weekType = index / 7
			
			val lessonsOfDay = allLessons.filter {
				it.section.weekType == weekType && it.section.dayOfWeek == dayOfWeek
			}
			DayPlan(weekType, dayOfWeek, lessonsOfDay)
		}.asList()
	}
	
	private fun refreshLessonList() {
		swipeRefreshLayout.isRefreshing = true
		launch(CommonPool) {
			val newDayPlans = tryOrNull { webSitePlan.loadLessons() }
				?.let { arrangeLessons(it) }
				?: kotlin.run {
					launch(UI) {
						Toast.makeText(getContext(), "获取课表信息失败！", Toast.LENGTH_SHORT).show()
						swipeRefreshLayout.isRefreshing = false
					}
					return@launch
				}
			
			val newOffset = PlanUtils.nowSection().run { weekType * 7 + dayOfWeek }
			launch(UI) {
				(lessonListView.adapter as DayListAdapter).run {
					dayPlans = newDayPlans
					offset = newOffset
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
