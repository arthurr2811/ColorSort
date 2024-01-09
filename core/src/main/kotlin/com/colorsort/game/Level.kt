package com.colorsort.game

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
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

    val spawner : Spawner = levelDef.spawner
    private val dispatcherLeft: Dispatcher = levelDef.dispatcherLeft
    private val dispatcherRight: Dispatcher = levelDef.dispatcherRight
    val dispatcherController: DispatcherController = levelDef.dispatcherController

    val ground : DestroyingBorder = levelDef.ground
    private val leftBorder : Border = levelDef.leftBorder
    private val rightBorder : Border = levelDef.rightBorder
    // world and collision
    private val world : World = levelDef.world
    private var contactListener : ContactListener = ContactListener(this)
    // game state
    var gameState = levelDef.gameState
    private val startScreen = levelDef.startScreen
    private val gameOverScreen = levelDef.gameOverScreen
    private val pauseScreen = levelDef.pauseScreen
    // for ball spawning
    var lastSpawnTime : Long = 0
    // score
    var score = 0
    var highScore = 0
    // world dimensions
    private val worldWidth = levelDef.worldWidth
    private val worldHeight = levelDef.worldHeight

    init {
        world.setContactListener(contactListener)
    }

    fun getTexts() : ArrayList<TextDrawHelper>{
        // draw score
        val texts = ArrayList<TextDrawHelper>()
        // draw score top center
        if (gameState != GameState.STARTSCREEN){
            texts.add(TextDrawHelper("Score", Vector2(worldWidth/2, 78f), Color.WHITE, 2f))
            texts.add(TextDrawHelper(score.toString(), Vector2(worldWidth/2, 76f), Color.WHITE, 2f))
        }
        // draw score and highScore
        if (gameState == GameState.GAMEOVER){
            texts.add(TextDrawHelper("Score:", Vector2(worldWidth/2, 45f), Color.BLACK, 4f))
            texts.add(TextDrawHelper(score.toString(), Vector2(worldWidth/2, 43f), Color.BLACK, 4f))
            texts.add(TextDrawHelper("High Score:", Vector2(worldWidth/2, 39f), Color.BLACK, 4f))
            texts.add(TextDrawHelper(highScore.toString(), Vector2(worldWidth/2, 37f), Color.BLACK, 4f))

        }
        // draw tutorial text at start of game
        if (score == 0 && gameState == GameState.INGAME){
            texts.add(TextDrawHelper("SWIPE TO MOVE THE TRIANGLES", Vector2(worldWidth/2, 45f), Color.BLACK, 4f))
            texts.add(TextDrawHelper("<---->", Vector2(worldWidth/2, 41f), Color.BLACK, 4f))
        }
        if (gameState == GameState.STARTSCREEN){
            texts.add(TextDrawHelper("High Score:", Vector2(worldWidth/2, 27f), Color.BLACK, 4f))
            texts.add(TextDrawHelper(highScore.toString(), Vector2(worldWidth/2, 24f), Color.BLACK, 4f))
        }
        return texts
    }

    // returns the textures, their position and scale, at next step.
    // 1 step is 1 iteration of the world (calculating all physics)
    fun getNextTexturePositions () : ArrayList<TextureDrawHelper> {
        if (gameState == GameState.INGAME){
            // step the world
            doStep()
        }
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
        // pause button
        textureDrawHelpers.add(TextureDrawHelper(Texture("PauseButton80_80.png"), Vector2(37f, 77f), Vector2(2f,2f)))
        // for start screen
        if (gameState == GameState.STARTSCREEN){
            textureDrawHelpers.add(TextureDrawHelper(startScreen.getTexture(), startScreen.getTexturePosition(), startScreen.getTextureSize()))
        }
        if (gameState == GameState.GAMEOVER){
            textureDrawHelpers.add(TextureDrawHelper(gameOverScreen.getTexture(), gameOverScreen.getTexturePosition(), gameOverScreen.getTextureSize()))
        }
        if (gameState == GameState.PAUSED){
            textureDrawHelpers.add(TextureDrawHelper(pauseScreen.getTexture(), pauseScreen.getTexturePosition(), pauseScreen.getTextureSize()))
        }

        return textureDrawHelpers
    }
    private fun doStep () {
        // check if need to spawn new ball and do so
        if (TimeUtils.timeSinceNanos(lastSpawnTime) > spawner.spawnInterval * 1_000_000_000L){
            ballsList.add(spawner.spawnBall())
            lastSpawnTime = TimeUtils.nanoTime()
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
    fun gameOver() {
        lastSpawnTime = TimeUtils.nanoTime()
        ballsToRemoveList.addAll(ballsList)
        gameState = GameState.GAMEOVER
    }
}
