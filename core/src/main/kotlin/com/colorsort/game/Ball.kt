package com.colorsort.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.circle
import kotlin.random.Random

class Ball (private var color : GameColor = GameColor.RANDOM, world: World, private val positionX : Float, private val positionY : Float) {
    private var ballBody : Body? = null
    private var ballFixture : Fixture? = null

    private val blueBallTexture by lazy { Texture("BlueBall.png") }
    private val greenBallTexture by lazy { Texture("GreenBall.png") }
    private val redBallTexture by lazy { Texture("RedBall.png") }
    init {
        if (color == GameColor.RANDOM){
            color = GameColor.entries[Random.nextInt(GameColor.entries.size - 1)]
        }
        // create physics2D body
        ballBody = world.body {
            position.set(positionX -20f, positionY- 20)
            type = BodyDef.BodyType.DynamicBody
            circle(radius = 20f)
            {disposeOfShape = true} }
        ballFixture = ballBody!!.circle(radius = 20f){
            density = 0.5f
            friction = 0.4f
            restitution = 0.1f
            disposeOfShape = true
        }
    }
    fun getColor() : GameColor {
        return this.color
    }
    fun getTexture(): Texture? {
        return when(color){
            GameColor.GREEN -> greenBallTexture
            GameColor.BLUE -> blueBallTexture
            GameColor.RED -> redBallTexture
            GameColor.RANDOM -> null
        }
    }
    fun getBallBody (): Body? {
        return this.ballBody
    }
    fun getBallFixture() : Fixture? {
        return this.ballFixture
    }
    fun destroyBody(world: World){
        val bodyToDestroy = this.getBallBody()
        this.ballBody = null
        // make sure to not remove something that's null!
        if (bodyToDestroy != null){
            world.destroyBody(bodyToDestroy)
        }
    }
}
