package com.colorsort.game.gameObjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.edge
// where the balls should land
class Hopper(private var color : GameColor, world: World, private val positionX : Float, private val positionY : Float) :
    GameObject {
    // physics
    private var hopperBody : Body? = null
    private var hopperFixture : Fixture? = null
    // textures
    private val blueHopperTexture by lazy { Texture("Hoppers/BlueHopper200_240.png") }
    private val greenHopperTexture by lazy { Texture("Hoppers/GreenHopper200_240.png") }
    private val redHopperTexture by lazy { Texture("Hoppers/RedHopper200_240.png") }
    init {
        // body is just a line on top of the hopper
        hopperBody = world.body {
            position.set(positionX, positionY)
        }
        hopperFixture = hopperBody!!.edge(from = Vector2(0f, 6f), to = Vector2(5f, 6f)){disposeOfShape = true}
    }

    fun getColor() : GameColor {
        return this.color
    }
    override fun getTexture(): Texture? {
        return when(color){
            GameColor.GREEN -> greenHopperTexture
            GameColor.BLUE -> blueHopperTexture
            GameColor.RED -> redHopperTexture
            GameColor.RANDOM -> null
        }
    }
    // edge fixtures dont have offset problem
    override fun getTexturePosition() : Vector2 {
        return Vector2(positionX, positionY)
    }
    override fun getTextureSize() : Vector2 {
        return Vector2(5f,6f)
    }
    override fun getBody (): Body? {
        return this.hopperBody
    }
    override fun getFixture() : Fixture? {
        return this.hopperFixture
    }
}
