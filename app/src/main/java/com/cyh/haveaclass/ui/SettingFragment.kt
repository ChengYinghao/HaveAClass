package com.cyh.haveaclass.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cyh.haveaclass.R
import com.cyh.haveaclass.control.MyApp
import kotlinx.android.synthetic.main.settings.*


class SettingFragment : Fragment() {
    companion object {
        fun newInstance(): SettingFragment {
            return SettingFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.settings, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myApp = context.applicationContext as MyApp
        val manager = myApp.settingsManager
        val builder = manager.settings.toBuilder()
        set_determine_button.setOnClickListener {
            // 获取用户输入的组号
            val groupName: String? = team_editText.text.toString()
            if (groupName != null) {
                val vary_builder = builder.setGroupName(groupName)
                manager.saveSettings(vary_builder.buildPartial())
            }

            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, LessonListFragment.newInstance(manager.loadSettings()!!.groupName))
                    .addToBackStack(null)
                    .commit()
        }
    }
}