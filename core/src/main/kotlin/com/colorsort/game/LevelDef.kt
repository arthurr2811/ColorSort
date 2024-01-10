package com.colorsort.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
// define a Level, has standard values for basic endless game, everything can be changed
class LevelDef () {
    val worldWidth = 40f
    val worldHeight = 80f

    val ballsList : ArrayList<Ball> = ArrayList()
    val ballsToRemoveList : ArrayList <Ball> = ArrayList()

    val hopperList : ArrayList<Hopper> = ArrayList()

    val obstacleList : ArrayList<Obstacle> = ArrayList()

    var world : World = World(Vector2(0f,-9.8f), true)
    var contactListener : ContactListener? = null

    var gameState = GameState.STARTSCREEN
    val gameOverScreen = GameOverScreen()
    val startScreen = StartScreen()
    val pauseScreen = GamePause()


    var spawner : Spawner = Spawner(worldWidth / 2, worldHeight - 5, 2f,world)
    var dispatcherLeft: Dispatcher = Dispatcher(world, 11f, worldHeight - 32f, DispatcherOrientation.LEFT)
    var dispatcherRight: Dispatcher = Dispatcher(world, 21f, worldHeight - 32f, DispatcherOrientation.RIGHT)
    var dispatcherController: DispatcherController = DispatcherController(dispatcherLeft, dispatcherRight, 0.3f)

    var ground : DestroyingBorder = DestroyingBorder(world, Vector2(0f, 0f), Vector2(40f, 0f), Vector2(0f, 0.1f))
    var leftBorder : Border = Border(world, 0.1f, 80f, Vector2(0.05f, 40f))
    var rightBorder : Border = Border(world, 0.1f, 80f, Vector2(39.95f, 40f))

    var gameOverSound = Gdx.audio.newSound(Gdx.files.internal("GameOverSound.mp3"))
    var ballCollissionSound = Gdx.audio.newSound(Gdx.files.internal("BallCollisionSound.mp3"))
    var scoreSound = Gdx.audio.newSound(Gdx.files.internal("ScoreSound.mp3"))
    val music = Gdx.audio.newMusic(Gdx.files.internal("MainMusic.mp3"))
    init {
        // init standard hopper setup (hopperList can of course be changed)
        val greenHopper = Hopper(GameColor.GREEN, world, worldWidth * 0.5f -2, 0f)
        hopperList.add(greenHopper)
        val blueHopper = Hopper(GameColor.RED, world, worldWidth * 0.2f -2, 0f)
        hopperList.add(blueHopper)
        val redHopper = Hopper(GameColor.BLUE, world, worldWidth * 0.8f -2, 0f)
        hopperList.add(redHopper)
        // init standard obstacle setup (obstacle List can of course be changed)
        val leftObstacle = Obstacle(world, worldWidth * 0.35f -2, worldHeight * 0.2f)
        obstacleList.add(leftObstacle)
        val rightObstacle = Obstacle(world, worldWidth * 0.65f -2, worldHeight * 0.2f)
        obstacleList.add(rightObstacle)
    }
}
