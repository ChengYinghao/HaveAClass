package com.cyh.haveaclass.ui

import android.app.Fragment
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import com.cyh.haveaclass.R
import com.cyh.haveaclass.core.Lesson
import com.cyh.haveaclass.core.PlanUtils
import com.cyh.haveaclass.core.WebSitePlan
import kotlinx.android.synthetic.main.fragment_lesson_list.*
import kotlinx.android.synthetic.main.search_lesson_fragment.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.util.*

/**
 * Created by CYH on 2018/4/7.
 */

class SearchLessonFragment : Fragment(), SearchView.OnQueryTextListener {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onQueryTextSubmit(query: String?): Boolean {
        refreshLessonList(query)
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onQueryTextChange(newText: String?): Boolean {
        refreshLessonList(newText)
        return false
    }

    companion object {
        private const val argName_groupName = "groupName"
        var SearchLessonFragment.argGroupName: String
            get() = arguments.getString(SearchLessonFragment.argName_groupName)
            set(value) {
                arguments = (arguments
                        ?: Bundle()).apply { putString(SearchLessonFragment.argName_groupName, value) }
            }
        fun newInstance(groupName: String): SearchLessonFragment {
            return SearchLessonFragment().apply { argGroupName = groupName }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.search_lesson_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searched_lessonListView.adapter = object : BaseAdapter() {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val lessonView = (convertView as? LessonView) ?: LessonView(context)
                lessonView.lesson = getItem(position)
                return lessonView
            }

            override fun getItem(position: Int): Lesson = search_lessonList[position]

            override fun getItemId(position: Int): Long = 0L

            override fun getCount(): Int = search_lessonList.size


        }
        search2.setOnQueryTextListener(this)
    }

    private var search_lessonList: List<Lesson> = emptyList()

    @RequiresApi(Build.VERSION_CODES.M)
    private fun refreshLessonList(query: String?) {
        if (query.equals(null)) {
            return
        }
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
                search_lessonList = allLessons.filter {
                    if (it.section.weekType == nowSection.weekType) {
                        it.name.indexOf(query!!) != 0 || it.teacher.indexOf(query) != 0
                    } else false
                }
                (lessonListView.adapter as BaseAdapter).notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private lateinit var webSitePlan: WebSitePlan

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webSitePlan = WebSitePlan(argGroupName)
    }
}