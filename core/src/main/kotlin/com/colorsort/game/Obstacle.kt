package com.colorsort.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.polygon

class Obstacle(world: World, private val positionX : Float, private val positionY : Float) : GameObject {
    private var obstacleBody : Body? = null
    private var obstacleFixture : Fixture? = null
    private val obstacleTexture by lazy { Texture("Obstacle200_120.png") }

    init {
        obstacleBody = world.body {
            position.set(positionX, positionY)
        }
        obstacleFixture = obstacleBody!!.polygon (Vector2(0f, 0f), Vector2(2.5f, 3f), Vector2(5f, 0f)){ disposeOfShape = true }
    }

    override fun getTexture(): Texture {
        return this.obstacleTexture
    }

    override fun getTexturePosition(): Vector2 {
        return Vector2(obstacleBody!!.position.x, obstacleBody!!.position.y)
    }

    override fun getTextureSize(): Vector2 {
        return Vector2(5f,3f)
    }

    override fun getBody(): Body? {
        return this.obstacleBody
    }

    override fun getFixture(): Fixture? {
        return this.obstacleFixture
    }

}
