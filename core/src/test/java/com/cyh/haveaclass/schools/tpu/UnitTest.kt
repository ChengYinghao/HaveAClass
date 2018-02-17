package com.cyh.haveaclass.schools.tpu

import org.junit.Test
import java.net.URL


class UnitTest {
    @Test
    fun testConnection() {
        val groupName = "150Б52"
        val url = "http://rasp.tpu.ru/view.php?for=$groupName&aslist=1&weekType=1"
        val connection = URL(url).openConnection()
        val html = connection.getInputStream().bufferedReader().readText()
        
        println(html)
    }
}
