package com.cyh.haveaclass.control.utils

inline fun <T> tryOrNull(body: () -> T): T? {
	return try {
		body()
	} catch (e: Exception) {
		e.printStackTrace()
		null
	}
}

inline fun tryOrFalse(body: () -> Unit): Boolean {
	return try {
		body()
		true
	} catch (e: Exception) {
		e.printStackTrace()
		false
	}
}

