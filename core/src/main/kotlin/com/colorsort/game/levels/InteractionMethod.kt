package com.colorsort.game.levels
/*
the different ways a player can control the movement of objects
1.) DIRECT: tap on a object, swipe along x axis to move it, taps and swipes anywhere else have no effect
2.) INDIRECT_TAP: tap on a object, swipe along x axis to move it, also swipes anywhere on screen are
                  possible, the movement is than mapped to the last tapped object
3.) INDIRECT_SWIPE: swipe alon X Axis anywhere to move the current selected object, swipe up or
                    down to switch selected object
 */
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

