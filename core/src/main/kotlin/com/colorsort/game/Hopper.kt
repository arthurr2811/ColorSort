package com.colorsort.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.edge

class Hopper(private var color : GameColor, world: World, positionX : Float, positionY : Float) {
    private var hopperBody : Body? = null
    private var hopperFixture : Fixture? = null
    init {
        // create body as a simple line reaching from top left to top right from hopper
        hopperBody = world.body {
            position.set(positionX - 35f, positionY)
            edge(from = Vector2(0f, 0f), to = Vector2(70f, 0f)){
                disposeOfShape = true
            }
        }
        hopperFixture = hopperBody!!.edge(from = Vector2(0f, 40f), to = Vector2(70f, 40f))
    }

    fun getColor() : GameColor {
        return this.color
    }
    fun getHopperBody (): Body? {
        return this.hopperBody
    }
    fun getHopperFixture() : Fixture? {
        return this.hopperFixture
    }
}
