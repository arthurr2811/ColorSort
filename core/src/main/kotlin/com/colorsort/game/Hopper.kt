package com.colorsort.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.edge

class Hopper(private var color : GameColor, world: World, private val positionX : Float, private val positionY : Float) {
    private var hopperBody : Body? = null
    private var hopperFixture : Fixture? = null
    // Textures
    private val blueHopperTexture by lazy { Texture("BlueHopper.png") }
    private val greenHopperTexture by lazy { Texture("GreenHopper.png") }
    private val redHopperTexture by lazy { Texture("RedHopper.png") }
    init {
        // create body as a simple line reaching from top left to top right from hopper
        hopperBody = world.body {
            position.set(positionX - 35f, positionY)
        }
        hopperFixture = hopperBody!!.edge(from = Vector2(0f, 70f), to = Vector2(70f, 70f)){disposeOfShape = true}
    }

    fun getColor() : GameColor {
        return this.color
    }
    fun getTexture(): Texture? {
        return when(color){
            GameColor.GREEN -> greenHopperTexture
            GameColor.BLUE -> blueHopperTexture
            GameColor.RED -> redHopperTexture
            GameColor.RANDOM -> null
        }
    }
    private fun getHopperBody (): Body? {
        return this.hopperBody
    }
    fun getTexturePosition() : Vector2 {
        return Vector2(positionX -35, positionY)
    }
    fun getHopperFixture() : Fixture? {
        return this.hopperFixture
    }
}
