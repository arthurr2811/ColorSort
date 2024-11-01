package com.colorsort.game.levels

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.TimeUtils
import com.colorsort.game.gameObjects.Ball
import com.colorsort.game.gameObjects.BallDestroyer
import com.colorsort.game.gameObjects.Border
import com.colorsort.game.gameObjects.Dispatcher
import com.colorsort.game.gameObjects.DispatcherController
import com.colorsort.game.gameObjects.Hopper
import com.colorsort.game.gameObjects.Obstacle
import com.colorsort.game.gameObjects.Spawner
import com.colorsort.game.helpers.ContactListener
import com.colorsort.game.helpers.TextDrawHelper
import com.colorsort.game.helpers.TextureDrawHelper
import com.colorsort.game.screens.GameState

/*
a level defined by given level definition. The idea is to set up everything in a levelDef. A level
Def can freely be adjusted. A level however cant be changed. It is defined by the given level def.
 */
class Level(levelDef: LevelDef)  {
    // game objects
    val ballsList : ArrayList<Ball> = levelDef.ballsList
    val ballsToRemoveList : ArrayList <Ball> = levelDef.ballsToRemoveList
    val hopperList : ArrayList<Hopper> = levelDef.hopperList
    private val obstacleList : ArrayList<Obstacle> = levelDef.obstacleList

    val spawner : Spawner = levelDef.spawner
    private val increaseSpawnInterval = levelDef.increaseSpawnInterval
    private val dispatcherLeft: Dispatcher = levelDef.dispatcherLeft
    private val dispatcherRight: Dispatcher = levelDef.dispatcherRight
    val dispatcherController: DispatcherController = levelDef.dispatcherController

    val ground : BallDestroyer = levelDef.ground
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
    private val settingsScreen = levelDef.settingsScreen
    // interaction method
    private var interactionMethod = levelDef.interactionMethod
    // for ball spawning
    var lastSpawnTime : Long = 0
    // score
    private var score = 0
    private var highScore = 0
    // world dimensions
    val worldWidth = levelDef.worldWidth
    val worldHeight = levelDef.worldHeight
    // sounds and music
    var soundVolume = levelDef.soundVolume
    var gameOverSound : Sound = levelDef.gameOverSound
    var ballCollisionSound : Sound = levelDef.ballCollisionSound
    var scoreSound : Sound = levelDef.scoreSound
    private val music : Music = levelDef.music
    private var playMusic = levelDef.playMusic
    private var playSound = levelDef.playSound

    init {
        world.setContactListener(contactListener)
        music.isLooping = true
        music.volume = 0.5f
        music.play()
        settingsScreen.level = this
    }

    // returns a List of TextDraw Helper containing every texts to be drawn and their position
    fun getTexts() : ArrayList<TextDrawHelper>{
        // draw score
        val texts = ArrayList<TextDrawHelper>()
        // draw score top center
        if (gameState != GameState.STARTSCREEN && gameState!= GameState.SETTINGS){
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

    // updates the world (so everything moves 1 step) and then returns a List of TextDraw Helper
    // containing every textures to be drawn and their position.
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
        textureDrawHelpers.add(TextureDrawHelper(Texture("ButtonsAndIcons/PauseButton80_80.png"), Vector2(37f, 77f), Vector2(2f,2f)))
        // for walls
        textureDrawHelpers.add(TextureDrawHelper(leftBorder.getTexture(), leftBorder.getTexturePosition(), leftBorder.getTextureSize()))
        textureDrawHelpers.add(TextureDrawHelper(rightBorder.getTexture(), rightBorder.getTexturePosition(), rightBorder.getTextureSize()))
        // for start screen
        if (gameState == GameState.STARTSCREEN){
            textureDrawHelpers.addAll(startScreen.getTexturePositions(playSound, playMusic))
        }
        if (gameState == GameState.GAMEOVER){
            textureDrawHelpers.add(TextureDrawHelper(gameOverScreen.getTexture(), gameOverScreen.getTexturePosition(), gameOverScreen.getTextureSize()))
        }
        if (gameState == GameState.PAUSED){
            textureDrawHelpers.add(TextureDrawHelper(pauseScreen.getTexture(), pauseScreen.getTexturePosition(), pauseScreen.getTextureSize()))
        }
        if (gameState == GameState.SETTINGS){
            textureDrawHelpers.addAll(settingsScreen.getTexturePositions(interactionMethod))
        }

        return textureDrawHelpers
    }
    // ToDo add selectedObject, which is either dispatcher or bottom obstacles: input updates selectedObject if necessary
    //  and then moves selectedObject (add obstacleController)
    fun handlePlayerInput(x: Float, y: Float, deltaX: Float?, deltaY : Float?){
        // decide based on interactionMOde what to do with input
        if (deltaX != null && deltaY != null){
            when(interactionMethod){
                InteractionMethod.DIRECT -> handleDirectInteraction(x, y, deltaX,deltaY)
                InteractionMethod.INDIRECT_TAP -> handleIndirectTapInteraction(deltaX,null, null)
                InteractionMethod.INDIRECT_SWIPE -> handleIndirectSwipeInteraction(deltaX,deltaY)
            }
        } else if (interactionMethod == InteractionMethod.INDIRECT_TAP){
            handleIndirectTapInteraction(null, null, x)
        }

    }
    private fun handleDirectInteraction(x : Float, y : Float, deltaX : Float, deltaY: Float){
        val xMargin = 7 // as dispatcher is 15 long
        val yMargin = 4 // as dispatcher is 5 high

        val dispatcherPosition = dispatcherController.getDispatcherPosition()
        // if player taped somewhere near dispatcher center, move dispatcher
        if (x > dispatcherPosition.x - xMargin && x < dispatcherPosition.x + xMargin
            && y > dispatcherPosition.y - yMargin && y < dispatcherPosition.y + yMargin){
            dispatcherController.moveDispatcher(deltaX)
        }
        // ToDo: handle objectSelection, move bottom obstacles, if tapped there
    }
    private fun handleIndirectTapInteraction(deltaX : Float?, x: Float?, y: Float?){
        // ToDo: if only x,y: it was objectSelection INput, select correspondingly dispatcher or obstacles
        //  if only deltaX it was movement: move selectedObject
    }
    private fun handleIndirectSwipeInteraction(deltaX : Float, deltaY: Float){
        // falls später mehrere hindernisse gesteuert: check ob eher x oder y swipe
        // falls x aktuell ausgewähltes bewegen, eher y: eins drüber oder drunter auswählen
        dispatcherController.moveDispatcher(deltaX)
        // ToDo add objectSelection with deltaY swipes, delta xSwipes move the selectedObject
        // etwa so: if(deltax < schwellenwert && delta y >deltx * 3) dann ganz klar ein swipe um anders zu steuern
    }
    private fun doStep () {
        // check if need to spawn new ball
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
        gameOverSound.dispose()
        ballCollisionSound.dispose()
        scoreSound.dispose()
        music.dispose()
    }
    fun getWorld () : World {
        return this.world
    }
    fun gameOver() {
        // reset everything at game over
        spawner.spawnInterval = spawner.defaultSpawnInterval
        lastSpawnTime = TimeUtils.nanoTime()
        ballsToRemoveList.addAll(ballsList)
        gameState = GameState.GAMEOVER
        if (score > highScore){
            // safe new high score
            highScore = score
        }
    }
    fun getHighScore(): Int {
        return highScore
    }
    fun setHighScore(highScore : Int){
        this.highScore = highScore
    }
    fun increaseScore (amount : Int){
        score += amount
        // decrease spawn interval by 20% every 5 points to increase difficulty
        if (score % 5 == 0 && increaseSpawnInterval){
            spawner.spawnInterval *= 0.8f
        }
    }
    fun resetScore() {
        score = 0
    }
    fun getScore () : Int{
        return score
    }
    fun soundOfOrOn() {
        if (playSound){
            soundVolume = 0f
            playSound = false
        } else {
            soundVolume = 1f
            playSound = true
        }
    }
    fun musicOfOrOn() {
        if (playMusic){
            music.volume = 0f
            playMusic = false
        } else {
            music.volume = 0.5f
            playMusic = true
        }
    }
    fun setInteractionMethod (interactionMethod: InteractionMethod){
        this.interactionMethod = interactionMethod
    }
    fun inputToSettingsMenu(x : Float, y : Float){
        settingsScreen.handleTouchInput(x, y)
    }
}
