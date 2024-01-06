package com.colorsort.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
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
    // ToDo: ball spawner, add other game objects classes same way as ball, render them,
    //  collision = delete, spawner no need to have physics, but spawner.spawn() function
    //  make both dispatcher triangels movable by player, add score, add menue and highscore
    // ToDO: USE DISTANT JOINT FOR DISPATCHER
    override fun create() {
        // world
        world = World(Vector2(0f,-10f), true)
        val screenX = 400f
        val screenY = 800f
        camera.setToOrtho(false, screenX, screenY)
        contactListener = ContactListener(world, ballsList, ballsToRemoveList)
        world.setContactListener(ContactListener(world, ballsList, ballsToRemoveList))
        // spawner
        spawner = Spawner(screenX / 2, screenY - 60, 2f,world)
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
            batch.draw(hopper.getTexture(), hopper.getHopperBody()!!.position.x, hopper.getHopperBody()!!.position.y)
        }
        // draw balls
        for (ball in ballsList){
            batch.draw(ball.getTexture(), ball.getBallBody()!!.position.x, ball.getBallBody()!!.position.y)
        }
        // draw spawner (after balls!)
        batch.draw(spawner.spawnerTexture, spawner.getPosition().x, spawner.getPosition().y)
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

    }
}
