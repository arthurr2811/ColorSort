package com.colorsort.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.TimeUtils

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class ColorSortMainGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    // Game Objects textures ToDO move to coreesponding classes

    private val dispatcherLeftTexture by lazy { Texture("DispatcherLeft.png") }
    private val dispatcherRightTexture by lazy { Texture("DispatcherRight.png") }
    private val obstacleTexture by lazy { Texture("Obstacle.png") }

    // Camera
    private val camera : OrthographicCamera = OrthographicCamera()
    // balls list with physics
    private val ballsList : ArrayList<Ball> = ArrayList()
    private val ballsToRemoveList : ArrayList <Ball> = ArrayList()
    private var lastSpawnTime : Long = 0
    // hopper list with physics
    private val hopperList : ArrayList<Hopper> = ArrayList()
    private lateinit var world : World
    private lateinit var contactListener : ContactListener
    // spawner
    private lateinit var spawner : Spawner
    // dispatcher
    private lateinit var dispatcherLeft: Dispatcher
    private lateinit var dispatcherRight: Dispatcher
    private lateinit var dispatcherController: DispatcherController
    // borders
    private lateinit var ground : Despawner
    private lateinit var leftBorder : Border
    private lateinit var rightBorder : Border
    /*
    ToDo: add score add highscore
    Ideas for later: start screen, pause button and menue, adjustable gamerule (spawnspeed etc.)
     */

    override fun create() {
        // world
        world = World(Vector2(0f,-10f), true)
        val screenX = 400f
        val screenY = 800f
        camera.setToOrtho(false, screenX, screenY)
        contactListener = ContactListener(ballsList, ballsToRemoveList)
        world.setContactListener(contactListener)
        // spawner
        spawner = Spawner(screenX / 2, screenY - 60, 2f,world)
        // dispatcher
        dispatcherLeft = Dispatcher(world, 95f, screenY - 325f, DispatcherOrientation.LEFT)
        dispatcherRight = Dispatcher(world, 235f, screenY - 325f, DispatcherOrientation.RIGHT)
        dispatcherController = DispatcherController(dispatcherLeft, dispatcherRight, 0.5f)
        Gdx.input.inputProcessor = GestureDetector(dispatcherController)
        // create hopper
        val greenHopper = Hopper(GameColor.GREEN, world, screenX * 0.5f, 0f)
        hopperList.add(greenHopper)
        val blueHopper = Hopper(GameColor.RED, world, screenX * 0.2f, 0f)
        hopperList.add(blueHopper)
        val redHopper = Hopper(GameColor.BLUE, world, screenX * 0.8f, 0f)
        hopperList.add(redHopper)
        // ground
        ground = Despawner(world, Vector2(0f, 0f), Vector2(400f, 0f), Vector2(0f, 5f))
        leftBorder = Border(world, 3f, 800f, Vector2(1.5f, 400f))
        rightBorder = Border(world, 3f, 800f, Vector2(398.5f, 400f))
    }

    override fun render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        // ball spawning
        val currentTime : Long = TimeUtils.nanoTime()
        if (TimeUtils.timeSinceNanos(lastSpawnTime) > spawner.spawnInterval * 1_000_000_000L){
            ballsList.add(spawner.spawnBall())
            lastSpawnTime = currentTime
        }
        // get Hooper bodies
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        // draw hoppers !only works with 3 hoppers because of spacing
        for (hopper in hopperList){
            batch.draw(hopper.getTexture(), hopper.getTexturePosition().x, hopper.getTexturePosition().y)
        }
        // draw dispatcher
        batch.draw(dispatcherLeftTexture, dispatcherLeft.getTexturePosition().x, dispatcherLeft.getTexturePosition().y)
        batch.draw(dispatcherRightTexture, dispatcherRight.getTexturePosition().x, dispatcherRight.getTexturePosition().y)
        // draw balls
        for (ball in ballsList){
            batch.draw(ball.getTexture(), ball.getTexturePosition().x, ball.getTexturePosition().y)
        }
        // draw spawner (after balls!)
        batch.draw(spawner.spawnerTexture, spawner.getTexturePosition().x, spawner.getTexturePosition().y)
        batch.end()

        // simulate physics
        world.step(1/60f,6,2)
        // remove to remove balls
        processBallsForRemoval()
    }
    private fun processBallsForRemoval() {
        for (ball in ballsToRemoveList){
            ballsList.remove(ball)
            ball.destroyBody(world)
        }
    }

    override fun dispose() {
        for (ball in ballsList){
            ball.getTexture()?.dispose()
        }
        for (hopper in hopperList){
            hopper.getTexture()?.dispose()
        }
        spawner.spawnerTexture.dispose()
        batch.dispose()
        dispatcherRightTexture.dispose()
        dispatcherLeftTexture.dispose()
    }
}
