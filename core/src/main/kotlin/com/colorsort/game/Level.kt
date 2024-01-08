package com.colorsort.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.TimeUtils
// a level defined by given level definition
class Level(levelDef: LevelDef)  {
    // objects
    val ballsList : ArrayList<Ball> = levelDef.ballsList
    val ballsToRemoveList : ArrayList <Ball> = levelDef.ballsToRemoveList
    val hopperList : ArrayList<Hopper> = levelDef.hopperList
    private val obstacleList : ArrayList<Obstacle> = levelDef.obstacleList

    private val spawner : Spawner = levelDef.spawner
    private val dispatcherLeft: Dispatcher = levelDef.dispatcherLeft
    private val dispatcherRight: Dispatcher = levelDef.dispatcherRight
    private val dispatcherController: DispatcherController = levelDef.dispatcherController

    val ground : DestroyingBorder = levelDef.ground
    private val leftBorder : Border = levelDef.leftBorder
    private val rightBorder : Border = levelDef.rightBorder
    // world and collision
    private val world : World = levelDef.world
    private var contactListener : ContactListener = ContactListener(this)
    // for ball spawning
    private var lastSpawnTime : Long = 0
    // score
    private var score = 0
    private var highScore = 0

    init {
        // init physics
        world.setContactListener(contactListener)
        Gdx.input.inputProcessor = GestureDetector(dispatcherController)
    }

    fun getTexts() : ArrayList<TextDrawHelper>{
        // draw score
        val texts = ArrayList<TextDrawHelper>()
        // draw score top center
        texts.add(TextDrawHelper("Score", Vector2(20f, 78f)))
        texts.add(TextDrawHelper(score.toString(), Vector2(20f, 76f)))
        return texts
    }

    // returns the textures, their position and scale, at next step.
    // 1 step is 1 iteration of the world (calculating all physics)
    fun getNextTexturePositions () : ArrayList<TextureDrawHelper> {
        // step the world
        doStep()
        // collect updated positions
        val textureDrawHelpers : ArrayList<TextureDrawHelper> = ArrayList()
        // for hopper
        for (hopper in hopperList){
            textureDrawHelpers.add(TextureDrawHelper(hopper.getTexture()!!, hopper.getTexturePosition(), hopper.getTextureSize()))
        }
        // for obstacles
        for (obstacle in obstacleList){
            textureDrawHelpers.add(TextureDrawHelper(obstacle.getTexture(), obstacle.getTexturePosition(), obstacle.getTextureSize()))
        }
        // for dispatcher
        textureDrawHelpers.add(TextureDrawHelper(dispatcherLeft.getTexture(), dispatcherLeft.getTexturePosition(), dispatcherLeft.getTextureSize()))
        textureDrawHelpers.add(TextureDrawHelper(dispatcherRight.getTexture(), dispatcherRight.getTexturePosition(), dispatcherRight.getTextureSize()))
        // for balls
        for (ball in ballsList){
            textureDrawHelpers.add(TextureDrawHelper(ball.getTexture()!!, ball.getTexturePosition(), ball.getTextureSize()))
        }
        // for spawner
        textureDrawHelpers.add(TextureDrawHelper(spawner.getTexture(), spawner.getTexturePosition(), spawner.getTextureSize()))

        return textureDrawHelpers
    }
    private fun doStep () {
        // check if need to spawn new ball and do so
        val currentTime : Long = TimeUtils.nanoTime()
        if (TimeUtils.timeSinceNanos(lastSpawnTime) > spawner.spawnInterval * 1_000_000_000L){
            ballsList.add(spawner.spawnBall())
            lastSpawnTime = currentTime
        }
        // step world
        world.step(1/60f,6,2)
        // remove out of game balls  AFTER world.step calculations
        processBallsForRemoval()
    }
    // removes out of game balls by removing from balls list and destroying body
    private fun processBallsForRemoval() {
        for (ball in ballsToRemoveList){
            ballsList.remove(ball)
            ball.destroyBody(world)
        }
        ballsToRemoveList.clear()
    }
    // dispose textures
    fun dispose () {
        for (ball in ballsList){
            ball.getTexture()?.dispose()
        }
        for (hopper in hopperList){
            hopper.getTexture()?.dispose()
        }
        spawner.getTexture().dispose()
        dispatcherRight.getTexture().dispose()
        dispatcherLeft.getTexture().dispose()
    }
    fun getWorld () : World {
        return this.world
    }
    fun updateScore (amount : Int){
        score += amount
        println(score)
    }
    fun setHighScore (value : Int){
        highScore = value
    }
    fun gameOver() {
        score = 0
        lastSpawnTime = TimeUtils.nanoTime()
    }
}
