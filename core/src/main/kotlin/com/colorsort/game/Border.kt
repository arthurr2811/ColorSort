package com.colorsort.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.box

class Border (world: World, width : Float, height : Float, bodyPosition : Vector2) {
    private var borderBody : Body? = null
    private var borderFixture : Fixture? = null
    init {
        borderBody = world.body {
            position.set(bodyPosition)
        }
        borderFixture = borderBody!!.box(width, height)
    }
}
