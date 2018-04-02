package com.cyh.haveaclass.control

import android.app.Application
import com.cyh.haveaclass.control.settings.SettingsManager

class MyApp : Application() {
	
	val settingsManager by lazy { SettingsManager(this) }

}