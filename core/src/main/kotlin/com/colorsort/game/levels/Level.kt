package com.colorsort.game.levels

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.TimeUtils
import com.colorsort.game.gameObjects.Ball
import com.colorsort.game.gameObjects.BallDestroyer
import com.colorsort.game.gameObjects.Border
import com.colorsort.game.gameObjects.Dispatcher
import com.colorsort.game.gameObjects.Hopper
import com.colorsort.game.gameObjects.Obstacle
import com.colorsort.game.gameObjects.Spawner
import com.colorsort.game.helpers.ContactListener
import com.colorsort.game.helpers.Mover
import com.colorsort.game.helpers.TextDrawHelper
import com.colorsort.game.helpers.TextureDrawHelper
import com.colorsort.game.screens.GameState
import kotlin.math.abs

/*
a level defined by given level definition. The idea is to set up everything in a levelDef. A level
Def can freely be adjusted. A level however cant be changed. It is defined by the given level def.
 */
class Level(levelDef: LevelDef)  {
    // game objects
    private val ballsList : ArrayList<Ball> = levelDef.ballsList
    private val ballsToRemoveList : ArrayList <Ball> = levelDef.ballsToRemoveList
    private val hopperList : ArrayList<Hopper> = levelDef.hopperList
    private val obstacleList : ArrayList<Obstacle> = levelDef.obstacleList

    val spawner : Spawner = levelDef.spawner
    private val increaseSpawnInterval = levelDef.increaseSpawnInterval
    private val dispatcherLeft: Dispatcher = levelDef.dispatcherLeft
    private val dispatcherRight: Dispatcher = levelDef.dispatcherRight
    val mover: Mover = levelDef.mover
    private var objectSelectedByPlayer = levelDef.objectSelectedByPlayer

    private val ground : BallDestroyer = levelDef.ground
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
    // score and statistics
    private var score = 0
    private var highScore = 0
    private var farTaps = 0
    private var veryFarTaps = 0
    // world dimensions
    val worldWidth = levelDef.worldWidth
    val worldHeight = levelDef.worldHeight
    // sounds and music
    private var soundVolume = levelDef.soundVolume
    private var gameOverSound : Sound = levelDef.gameOverSound
    private var ballCollisionSound : Sound = levelDef.ballCollisionSound
    private var scoreSound : Sound = levelDef.scoreSound
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
            // show HighScore
            texts.add(TextDrawHelper("High Score:", Vector2(worldWidth/2, 30f), Color.BLACK, 4f))
            texts.add(TextDrawHelper(highScore.toString(), Vector2(worldWidth/2, 27f), Color.BLACK, 4f))
            // show TouchStatistics
            texts.add(TextDrawHelper("Tap Statistics:", Vector2(worldWidth/2, 24f), Color.BLACK, 4f))
            texts.add(TextDrawHelper("Far Taps: $farTaps, Very Far Taps: $veryFarTaps",
                Vector2(worldWidth/2, 21f), Color.BLACK, 4f))
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
        // for other screens, which are rendered over the game
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

    // handle player inputs
    fun handlePlayerInput(x: Float, y: Float, deltaX: Float?, deltaY : Float?){
        // update statistics
        updateTapStatistics(x,y)
        // decide based on interactionMode what to do with input
        if (deltaX != null && deltaY != null){
            when(interactionMethod){
                InteractionMethod.DIRECT -> handleDirectInteraction(x, y, deltaX)
                InteractionMethod.INDIRECT_TAP -> handleIndirectTapInteraction(deltaX,x, y)
                InteractionMethod.INDIRECT_SWIPE -> handleIndirectSwipeInteraction(deltaX,deltaY)
            }
        } else if (interactionMethod == InteractionMethod.INDIRECT_TAP){
            handleIndirectTapInteraction(null, x, y)
        }

    }
    private fun updateTapStatistics(x : Float, y : Float){
        // if a tap is within 75% of game world size its assumed to be normal tap,
        // outside 75 % but inside 90% its a far tap and everything further is a very far tap

        // far taps
        if (((x < 10 && x > 5) || (x > 30 && x < 35) || (y < 20 && y > 10) || (y > 60 && y < 70)) && farTaps < 999){
            farTaps++
        }
        // very far taps
        if ((x < 5 || x > 35 || y < 10 || y > 70) && veryFarTaps < 999){
            veryFarTaps++
        }
    }
    private fun handleDirectInteraction(x : Float, y : Float, deltaX : Float){
        // move the object, the player swiped on
        if (tappedDispatcher(x,y)){
            switchToDispatcher()
            mover.moveDispatcher(deltaX)
        }
        if (tappedObstacles(x,y)){
            switchToObstacles()
            mover.moveObstacles(deltaX)
        }
    }
    private fun handleIndirectTapInteraction(deltaX : Float?, x: Float, y: Float){
        // move the object, the player swiped on, or if swiped anywhere else move the last used object
        if (tappedDispatcher(x,y)){
            switchToDispatcher()
        }
        if (tappedObstacles(x,y)){
            switchToObstacles()
        }
        if (deltaX != null){
            if (objectSelectedByPlayer == MovableObjects.DISPATCHER){
                mover.moveDispatcher(deltaX)
            } else {
                mover.moveObstacles(deltaX)
            }
        }
    }
    private fun handleIndirectSwipeInteraction(deltaX : Float, deltaY: Float){
        // up and down swipes switch target, left and right swipes move target
        // unclear inputs should rather count as move swipes, switch target swipes will be rare
        // and should therefore be very clear (deltaY >>> deltaX)
        if (abs(deltaY) > 0.5 && abs(deltaY) > abs(deltaX) * 5){
            if (deltaY < 0){
                switchToDispatcher()
            } else{
                switchToObstacles()
            }
        } else {
            if (objectSelectedByPlayer == MovableObjects.DISPATCHER){
                mover.moveDispatcher(deltaX)
            } else {
                mover.moveObstacles(deltaX)
            }
        }
    }
    private fun tappedDispatcher (x :Float, y: Float) : Boolean{
        val dispatcherPosition = mover.getDispatcherPosition()
        val xMarginDisp = 7
        val yMarginDisp = 4
        return (x > dispatcherPosition.x - xMarginDisp && x < dispatcherPosition.x + xMarginDisp
                && y > dispatcherPosition.y - yMarginDisp && y < dispatcherPosition.y + yMarginDisp)
    }
    private fun tappedObstacles (x :Float, y: Float) : Boolean{
        val obstaclePosition = mover.getObstaclePosition()
        val xMarginObst = 12
        val yMarginObst = 3
        return (x > obstaclePosition.x - xMarginObst && x < obstaclePosition.x + xMarginObst
            && y > obstaclePosition.y - yMarginObst && y < obstaclePosition.y + yMarginObst)
    }
    private fun switchToDispatcher () {
        if (objectSelectedByPlayer != MovableObjects.DISPATCHER){
            objectSelectedByPlayer = MovableObjects.DISPATCHER
            dispatcherLeft.select()
            dispatcherRight.select()
            obstacleList[0].unSelect()
            obstacleList[1].unSelect()
        }
    }
    private fun switchToObstacles () {
        if (objectSelectedByPlayer != MovableObjects.OBSTACLES){
            objectSelectedByPlayer = MovableObjects.OBSTACLES
            dispatcherLeft.unSelect()
            dispatcherRight.unSelect()
            obstacleList[0].select()
            obstacleList[1].select()
        }
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
    // contactListener only gets the fixtures, which collide so we need this functions to check
    // from what objects the fixtures came
    fun findBallFromFixture(fixture: Fixture?) : Ball?{
        // if any ball.getfixure() in ball ist -> fixture is from a ball
        return ballsList.find { it.getFixture() == fixture }
    }
    fun findHopperFromFixture(fixture: Fixture?) : Hopper?{
        // if any hopper.getfixure() in hopperList -> fixture is from a hopper
        return hopperList.find { it.getFixture() == fixture }
    }
    fun findDestrBoarderFromFixture (fixture: Fixture?) : BallDestroyer? {
        return if(fixture == ground.getFixture()){
            ground
        } else {
            null
        }
    }
    fun deleteBall(ball : Ball?) {
        // DON'T REMOVE DIRECTLY we are in a physics step:
        // https://www.iforce2d.net/b2dtut/removing-bodies#:~:text=The%20actual%20code%20to%20remove,timestep%2C%20usually%20a%20collision%20callback.
        // add ball to remove list, remove later
        if (ball != null) {
            if (!ballsToRemoveList.contains(ball)) {
                ballsToRemoveList.add(ball)
            }
        }
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
    fun resetTapStatistics() {
        farTaps = 0
        veryFarTaps = 0
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
    fun soundOfOrOn(on : Boolean) {
        if (on){
            soundVolume = 1f
            playSound = true
        } else {
            soundVolume = 0f
            playSound = false
        }
    }
    fun isSoundOn () : Boolean{
        return playSound
    }
    fun isMusicOn () : Boolean{
        return playMusic
    }
    fun musicOfOrOn(on : Boolean) {
        if (on){
            music.volume = 0.5f
            playMusic = true
        } else {
            music.volume = 0f
            playMusic = false
        }
    }
    fun playScoreSound(){
        scoreSound.play(soundVolume)
    }
    fun playCollisionSound() {
        ballCollisionSound.play(soundVolume)
    }
    fun playGameOverSound() {
        gameOverSound.play(soundVolume)
    }
    fun setInteractionMethod (interactionMethod: InteractionMethod){
        this.interactionMethod = interactionMethod
    }
    fun getInteractionMethod () : InteractionMethod{
        return this.interactionMethod
    }
    fun inputToSettingsMenu(x : Float, y : Float){
        settingsScreen.handleTouchInput(x, y)
    }
}
