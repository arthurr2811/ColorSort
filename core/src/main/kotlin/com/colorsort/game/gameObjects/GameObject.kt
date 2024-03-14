package com.colorsort.game.gameObjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
/*
interface for general game object. Every object in this game might have a texture, position and
size. For physics (box2D) we also need a Body and Fixture
 */
interface GameObject {
    fun getTexture(): Texture?
    fun getTexturePosition() : Vector2
    fun getTextureSize() : Vector2
    fun getBody (): Body?
    fun getFixture() : Fixture?


}
