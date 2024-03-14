package com.colorsort.game.screens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
// what to render over current game when game over
class GameOverScreen {
    private val gameOverScreenTexture by lazy { Texture("Screens/GameOver300_400.png") }
    fun getTexture(): Texture {
        return this.gameOverScreenTexture
    }
    // offset so texture lays over the spawn point of balls
    fun getTexturePosition () : Vector2 {
        return Vector2(5f, 20f)
    }
    fun getTextureSize() : Vector2 {
        return Vector2(30f,40f)
    }
}
