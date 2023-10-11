package kr.cosine.fishing.enums

enum class Weather {
    SUN,
    RAIN,
    ALL;

    fun switch(): Weather {
        return when (this) {
            SUN -> RAIN
            RAIN -> ALL
            ALL -> SUN
        }
    }
}