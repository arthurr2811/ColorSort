package com.colorsort.game.levels

enum class InteractionMethod {
    DIRECT, INDIRECT_TAP, INDIRECT_SWIPE;

    fun toInt() : Int {
        return when(this){
            DIRECT -> 1
            INDIRECT_TAP -> 2
            INDIRECT_SWIPE -> 3
        }
    }
    companion object {
        fun fromInt(value: Int): InteractionMethod? {
            return when (value) {
                1 -> DIRECT
                2 -> INDIRECT_TAP
                3 -> INDIRECT_SWIPE
                else -> null
            }
        }
    }
}

