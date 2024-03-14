package com.colorsort.game.gameObjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.box

class Border (world: World, private val width : Float, private val height : Float, private val bodyPosition : Vector2) :
    GameObject {
    private var borderBody : Body? = null
    private var borderFixture : Fixture? = null
    private val borderTexture by lazy { Texture("Wall4_800.png") }
    init {
        borderBody = world.body {
            position.set(bodyPosition)
        }
        borderFixture = borderBody!!.box(width, height)
    }

    override fun getTexture(): Texture {
        return borderTexture
    }

    override fun getTexturePosition(): Vector2 {
        // if left border - texture size
        if (bodyPosition.x < 20){
            return Vector2(bodyPosition.x -0.5f, bodyPosition.y -40f)
        } else {
            return Vector2(bodyPosition.x, bodyPosition.y -40f)

        }
    }

    override fun getTextureSize(): Vector2 {
        return Vector2(0.5f, height - 25f)
    }

    override fun getBody(): Body? {
        return borderBody
    }

    override fun getFixture(): Fixture? {
        return borderFixture
    }
}
