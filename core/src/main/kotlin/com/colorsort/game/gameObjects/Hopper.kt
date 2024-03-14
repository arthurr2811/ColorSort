package com.colorsort.game.gameObjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.edge
/*
tube-like objects, which collect the balls. If color of hopper and ball match a point is scored
if not game over. It has a color, position and exists in a world (box2D)
 */
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
        // we can define the body just as a line on the hopper opening as we only need to check
        // for collisions there
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
