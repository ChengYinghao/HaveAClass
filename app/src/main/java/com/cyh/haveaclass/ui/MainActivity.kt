package com.cyh.haveaclass.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.cyh.haveaclass.R

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, LessonListFragment.newInstance("150Б52"))
                    .addToBackStack("今天课程")
                    .commit()

    }

}
