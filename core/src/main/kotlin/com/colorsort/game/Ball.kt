package com.colorsort.game

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.circle
import kotlin.random.Random

class Ball (private var color : GameColor = GameColor.RANDOM, world: World, positionX : Float, positionY : Float) {
    private var ballBody : Body? = null
    private var ballFixture : Fixture? = null
    init {
        if (color == GameColor.RANDOM){
            color = GameColor.entries[Random.nextInt(GameColor.entries.size - 1)]
        }
        // create physics2D body
        ballBody = world.body {
            position.set(positionX, positionY)
            type = BodyDef.BodyType.DynamicBody
            circle(radius = 20f)
            {disposeOfShape = true} }
        ballFixture = ballBody!!.circle(radius = 20f){
            density = 0.5f
            friction = 0.4f
        }
    }
    fun getColor() : GameColor {
        return this.color
    }
    fun getBallBody (): Body? {
        return this.ballBody
    }
    fun getBallFixture() : Fixture? {
        return this.ballFixture
    }

}
