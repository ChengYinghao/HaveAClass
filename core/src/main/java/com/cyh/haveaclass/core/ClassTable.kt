package com.cyh.haveaclass.core


interface ClassTable {
	fun allClasses(): Collection<AClass>
	fun selectClass(day: Int, section: Int): Collection<AClass>
	fun addAClass(aClass: AClass)
	
}
