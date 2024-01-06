package com.colorsort.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.edge

class Border(world: World, statPoint : Vector2, endPoint : Vector2) {
    private var borderBody : Body? = null
    private var borderFixture : Fixture? = null
    init {
        borderBody = world.body {
            position.set(0f, 0f)
            edge(from = statPoint, to = endPoint)
        }
        borderFixture = borderBody!!.edge(from = statPoint, to = endPoint)
    }
}
