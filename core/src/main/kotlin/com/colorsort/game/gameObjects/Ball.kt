package com.colorsort.game.gameObjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.circle
import kotlin.random.Random
/*
a ball is a GameObject. It has a color, position and exists in a world (box2D)
 */
class Ball (private var color : GameColor = GameColor.RANDOM, world: World, private val positionX : Float, private val positionY : Float) :
    GameObject {
    // physics
    private var ballBody : Body? = null
    private var ballFixture : Fixture? = null
    // textures
    private val blueBallTexture by lazy { Texture("Balls/BlueBall80_80.png") }
    private val greenBallTexture by lazy { Texture("Balls/GreenBall80_80.png") }
    private val redBallTexture by lazy { Texture("Balls/RedBall80_80.png") }
    init {
        if (color == GameColor.RANDOM){
            color = GameColor.entries[Random.nextInt(GameColor.entries.size - 1)]
        }
        // create physics2D body and fixture
        ballBody = world.body {
            position.set(positionX, positionY)
            type = BodyDef.BodyType.DynamicBody
        }
        ballFixture = ballBody!!.circle(radius = 1f){
            density = 1f
            friction = 1f
            restitution = 0.3f
            disposeOfShape = true
        }
    }
    fun getColor() : GameColor {
        return this.color
    }
    override fun getTexture(): Texture? {
        return when(color){
            GameColor.GREEN -> greenBallTexture
            GameColor.BLUE -> blueBallTexture
            GameColor.RED -> redBallTexture
            GameColor.RANDOM -> null
        }
    }
    // the texture is rendered from bottom left, the body position is in center of body
    // so we need to offset the texture by half the shapes size
    override fun getTexturePosition(): Vector2 {
        return Vector2(ballBody!!.position.x - 1f, ballBody!!.position.y - 1f)
    }
    override fun getTextureSize() : Vector2 {
        return Vector2(2f,2f)
    }
    override fun getBody (): Body? {
        return this.ballBody
    }
    override fun getFixture() : Fixture? {
        return this.ballFixture
    }
    fun destroyBody(world: World){
        val bodyToDestroy = this.getBody()
        // do not destroy body before setting ballBody null!
        this.ballBody = null
        if (bodyToDestroy != null){
            world.destroyBody(bodyToDestroy)
        }
    }
}
