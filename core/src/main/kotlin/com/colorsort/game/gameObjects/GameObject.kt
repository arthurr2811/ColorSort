package com.colorsort.game.gameObjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture

interface GameObject {
    fun getTexture(): Texture?
    fun getTexturePosition() : Vector2
    fun getTextureSize() : Vector2
    fun getBody (): Body?
    fun getFixture() : Fixture?


}
