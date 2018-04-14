package com.cyh.haveaclass.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cyh.haveaclass.R
import com.cyh.haveaclass.control.MyApp

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val app = applicationContext as MyApp
        val manager = app.settingsManager
        if (savedInstanceState == null) {
            val groupName = manager.settings.groupName
            if (groupName.equals("")) {
                supportFragmentManager.beginTransaction()
                        .add(R.id.fragmentContainer, SettingFragment.newInstance())
                        .commit()
            } else supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, LessonListFragment.newInstance(groupName))
                    .commit()
        }
    }

}
