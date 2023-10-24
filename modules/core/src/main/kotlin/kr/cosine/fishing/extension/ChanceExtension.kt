package kr.cosine.fishing.extension

import kotlin.math.ln
import kotlin.random.Random

private val random: Random by lazy { Random }

internal fun Double.chance(): Boolean {
    return if (this >= 100.0) true
    else if (this <= 0.0) false
    else {
        val successful = this
        val fail = 100.0 - successful
        mapOf(true to successful, false to fail).random()
    }
}

internal fun Int.chance(): Boolean = toDouble().chance()

internal fun <T> Map<T, Double>.random(): T {
    val entry = entries.minByOrNull { -ln(random.nextDouble()) / it.value }
    return entry?.key ?: throw IllegalArgumentException()
}