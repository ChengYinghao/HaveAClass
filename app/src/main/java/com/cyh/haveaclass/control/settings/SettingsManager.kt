package com.cyh.haveaclass.control.settings

import android.content.Context
import com.cyh.haveaclass.control.SettingsProto.Settings
import com.cyh.haveaclass.control.utils.tryOrFalse
import com.cyh.haveaclass.control.utils.tryOrNull

class SettingsManager(val context: Context) {
	
	companion object {
		const val settingsFileName = "settings"
	}
	
	val settings: Settings = loadSettings() ?: resetSettings()
	
	fun resetSettings(): Settings {
		val defaultSettings = Settings.getDefaultInstance()
		saveSettings(defaultSettings)
		return defaultSettings
	}
	
	fun saveSettings(settings: Settings): Boolean = tryOrFalse {
		val fileOutputStream = context.openFileOutput(settingsFileName, Context.MODE_PRIVATE)
		settings.writeTo(fileOutputStream)
	}
	
	fun loadSettings(): Settings? = tryOrNull {
		val fileInputStream = context.openFileInput(settingsFileName)
		Settings.parseFrom(fileInputStream)
	}
	
}

