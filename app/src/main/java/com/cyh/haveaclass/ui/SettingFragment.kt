package com.cyh.haveaclass.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cyh.haveaclass.R
import kotlinx.android.synthetic.main.settings.*


/**
 * Created by CYH on 2018/4/13.
 */

class SettingFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.settings, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        set_determine_button.setOnClickListener{
            // 获取用户输入的组号
            val groupName: Editable? = team_editText.text
        }
    }
}