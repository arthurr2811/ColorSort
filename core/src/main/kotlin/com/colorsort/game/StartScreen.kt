package com.colorsort.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
// screen shown at start of game
class StartScreen {
    private val startScreenTexture by lazy { Texture("StartScreen400_800.png") }
    fun getTexture(): Texture {
        return this.startScreenTexture
    }
    fun getTexturePosition () : Vector2 {
        return Vector2(0f, 0f)
    }
    fun getTextureSize() : Vector2 {
        return Vector2(40f,80f)
    }
}
