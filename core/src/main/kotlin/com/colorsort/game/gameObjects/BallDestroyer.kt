package com.colorsort.game.gameObjects

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.edge
/*
sits on the bottom and destroys balls when they are out of the game. Otherwise they would fall
endlessly in the void. Basically line between start and end point. It exists in a world (box2D)
 */
class BallDestroyer(world: World, statPoint : Vector2, endPoint : Vector2, bodyPosition : Vector2) {
    private var borderBody : Body? = null
    private var borderFixture : Fixture? = null
    init {
        borderBody = world.body {
            position.set(bodyPosition)
        }
        borderFixture = borderBody!!.edge(from = statPoint, to = endPoint)
    }
    fun getFixture() : Fixture? {
        return this.borderFixture
    }
}
