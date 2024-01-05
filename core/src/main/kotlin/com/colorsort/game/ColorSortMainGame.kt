package com.colorsort.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.FixtureDef
import com.badlogic.gdx.physics.box2d.PolygonShape
import com.badlogic.gdx.physics.box2d.World

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class ColorSortMainGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val image by lazy { Texture("testBall.png") }
    private val ground by lazy { Texture("ground.png") }
    private val camera : OrthographicCamera = OrthographicCamera()
    private lateinit var body : Body
    private lateinit var world : World

    override fun create() {
        // world
        world = World(Vector2(0f,-10f), true)
        camera.setToOrtho(false, 400f, 800f)
        val screenX = 400f
        val screenY = 800f

        // ball
        val bodyDef = BodyDef()
        bodyDef.type = BodyDef.BodyType.DynamicBody
        // start position top middle
        val positionX = screenX / 2f
        val positionY = screenY * 0.9f
        bodyDef.position.set(positionX, positionY)
        body = world.createBody(bodyDef)
        val fixtureDef = FixtureDef()
        val circle = CircleShape()
        circle.radius = 6f
        fixtureDef.shape = circle
        fixtureDef.density = 0.5f
        fixtureDef.friction = 0.1f
        fixtureDef.restitution = 0.6f
        circle.dispose()
        val fixture : Fixture = body.createFixture(fixtureDef)

        // ground bottom midddle
        val groundBodyDef = BodyDef()
        groundBodyDef.position.set(Vector2(0f,10f))
        val groundBody = world.createBody(groundBodyDef)
        val groundBox = PolygonShape()
        groundBox.setAsBox(screenX / 2, screenY * 0.1f)
        groundBody.createFixture(groundBox, 0.0f)
        groundBox.dispose()
    }
    override fun render() {
        //ball textur testweise mittig oben platzieren
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val x = body.position.x
        var y = body.position.y
        // ball löschen wenn auf boden aufkommt

        if (body.position.y <= 100f){
            world.destroyBody(body)
            y = 1000f
        }

        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        if (y < 800){
            batch.draw(image, x, y)
        }
        batch.draw(ground, 0f, 10f)
        batch.end()

        // physics 2D testing
        world.step(1/60f,6,2)
    }

    override fun dispose() {
        batch.dispose()
        image.dispose()
        ground.dispose()
    }
}
