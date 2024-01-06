package com.colorsort.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class ColorSortMainGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    // Game Objects textures
    private val spawner by lazy { Texture("DispatcherLeft.png") }
    private val dispatcherLeft by lazy { Texture("DispatcherLeft.png") }
    private val dispatcherRight by lazy { Texture("DispatcherRight.png") }
    private val obstacle by lazy { Texture("Obstacle.png") }
    private val blueHopper by lazy { Texture("BlueHopper.png") }
    private val greenHopper by lazy { Texture("GreenHopper.png") }
    private val redHopper by lazy { Texture("RedHopper.png") }
    private val blueBall by lazy { Texture("BlueBall.png") }
    private val greenBall by lazy { Texture("GreenBall.png") }
    private val redBall by lazy { Texture("RedBall.png") }

    // Camera
    private val camera : OrthographicCamera = OrthographicCamera()
    // balls list with physics
    private val ballsList : ArrayList<Ball> = ArrayList()
    // hopper list with physics
    private val hopperList : ArrayList<Hopper> = ArrayList()
    private lateinit var world : World
    // ToDO: ball spawner, add other game objects classes same way as ball, render them,
    //  collision = delete, spawner no need to have physics, but spawner.spawn() function
    //  make both dispatcher triangels movable by player, add score, add menue and highscore
    override fun create() {
        // world
        world = World(Vector2(0f,-10f), true)
        val screenX = 400f
        val screenY = 800f
        camera.setToOrtho(false, screenX, screenY)


        // create ball, add to balls list
        val testBallGreen = Ball(GameColor.RANDOM, world, (screenX/2), screenY - 60)
        ballsList.add(testBallGreen)
        // create hopper
        val greenHopper = Hopper(GameColor.GREEN, world, screenX * 0.5f, 0f)
        hopperList.add(greenHopper)
        val blueHopper = Hopper(GameColor.RED, world, screenX * 0.2f, 0f)
        hopperList.add(blueHopper)
        val redHopper = Hopper(GameColor.BLUE, world, screenX * 0.8f, 0f)
        hopperList.add(redHopper)
    }
    override fun render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        // get current position of first ball in balls list and needed color for texture
        val ball1TextureX = ballsList[0].getBallBody()!!.position.x
        val ball1TextureY = ballsList[0].getBallBody()!!.position.y
        var ball1Texture = greenBall
        if (ballsList[0].getColor() == GameColor.RED){
            ball1Texture = redBall
        } else if (ballsList[0].getColor() == GameColor.BLUE){
            ball1Texture = blueBall
        }
        // get Hooper bodies
        val greenHopperBody = hopperList[0].getHopperBody()
        val blueHopperBody = hopperList[1].getHopperBody()
        val redHopperBody = hopperList[2].getHopperBody()
        println(greenHopperBody!!.position.x)
        println(redHopperBody!!.position.x)
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        // draw corresponding texture on current position
        batch.draw(ball1Texture, ball1TextureX, ball1TextureY)
        batch.draw(greenHopper, greenHopperBody!!.position.x, greenHopperBody.position.y)
        batch.draw(blueHopper, blueHopperBody!!.position.x, blueHopperBody.position.y)
        batch.draw(redHopper, redHopperBody!!.position.x, redHopperBody.position.y)
        batch.end()

        // physics 2D testing
        world.step(1/60f,6,2)
    }

    override fun dispose() {
        greenBall.dispose()
        greenHopper.dispose()
        redHopper.dispose()
        blueHopper.dispose()
        batch.dispose()

    }
}
