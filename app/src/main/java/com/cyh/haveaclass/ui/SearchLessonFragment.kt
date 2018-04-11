package com.cyh.haveaclass.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import com.cyh.haveaclass.R
import com.cyh.haveaclass.core.Lesson
import com.cyh.haveaclass.core.PlanUtils
import com.cyh.haveaclass.core.WebSitePlan
import kotlinx.android.synthetic.main.search_lesson_fragment.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch


class SearchLessonFragment : Fragment(), SearchView.OnQueryTextListener, OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
        searchMode = SearchMode.BYNAME
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 1) {
            searchMode = SearchMode.BYDAYOFWEEK
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        refreshLessonList(query, searchMode)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        refreshLessonList(newText, searchMode)
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

            override fun getItem(position: Int): Lesson = searchLessonList[position]

            override fun getItemId(position: Int): Long = 0L

            override fun getCount(): Int = searchLessonList.size


        }
        search2.setOnQueryTextListener(this)
        search_spinner.onItemSelectedListener = this
    }

    private var searchLessonList: List<Lesson> = emptyList()

    private fun refreshLessonList(query: String?, searchMode: SearchMode) {
        if (query.equals(null)) {
            return
        }
        search_swipeRefreshLayout.isRefreshing = true
        launch {
            try {
                webSitePlan.fetchLessons()
            } catch (e: Exception) {
                launch(UI) {
                    Toast.makeText(getContext(), "获取课表信息失败！", Toast.LENGTH_SHORT).show()
                    search_swipeRefreshLayout.isRefreshing = false
                }
                return@launch
            }
            launch(UI) {
                val allLessons = webSitePlan.allLessons()
                searchLessonList = if (searchMode == SearchMode.BYNAME) {
                    allLessons.filter {
                        it.name.toLowerCase().indexOf(query!!.toLowerCase()) != -1 || it.teacher.toLowerCase().indexOf(query.toLowerCase()) != -1
                    }
                } else {
                    allLessons.filter {
                        PlanUtils.dayOfWeekToText(it.section.dayOfWeek).toLowerCase().indexOf(query!!.toLowerCase()) != -1
                    }
                }
                (searched_lessonListView.adapter as BaseAdapter).notifyDataSetChanged()
                search_swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private lateinit var webSitePlan: WebSitePlan
    private var searchMode: SearchMode = SearchMode.BYNAME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        webSitePlan = WebSitePlan(argGroupName)
    }
}

enum class SearchMode {
    BYNAME, BYDAYOFWEEK
}