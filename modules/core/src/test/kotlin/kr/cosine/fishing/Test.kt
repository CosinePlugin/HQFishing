package kr.cosine.fishing

import org.junit.jupiter.api.Test

class Test {

    private val timeRegex = Regex("^(\\d{1,5})\\s*~\\s*(\\d{1,5})\$")

    @Test
    fun regex_test() {
        val time = "0~15451"
        println(timeRegex.matches(time))
    }
}