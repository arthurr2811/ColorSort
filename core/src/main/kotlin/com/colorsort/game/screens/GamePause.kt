package com.colorsort.game.screens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
// what to render over current game when its paused
class GamePause {
    private val gamePauseTexture by lazy { Texture("Screens/Pause400_800.png") }
    fun getTexture(): Texture {
        return gamePauseTexture
    }
    fun getTexturePosition () : Vector2 {
        return Vector2(0f, 0f)
    }
    fun getTextureSize() : Vector2 {
        return Vector2(40f,80f)
    }
}
