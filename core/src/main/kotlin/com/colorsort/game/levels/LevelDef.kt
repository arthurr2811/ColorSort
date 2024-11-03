package com.colorsort.game.levels

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import com.colorsort.game.gameObjects.Ball
import com.colorsort.game.gameObjects.BallDestroyer
import com.colorsort.game.gameObjects.Border
import com.colorsort.game.gameObjects.Dispatcher
import com.colorsort.game.gameObjects.DispatcherOrientation
import com.colorsort.game.gameObjects.GameColor
import com.colorsort.game.gameObjects.Hopper
import com.colorsort.game.gameObjects.Mover
import com.colorsort.game.gameObjects.Obstacle
import com.colorsort.game.gameObjects.Spawner
import com.colorsort.game.screens.GameOverScreen
import com.colorsort.game.screens.GamePause
import com.colorsort.game.screens.GameState
import com.colorsort.game.screens.SettingsScreen
import com.colorsort.game.screens.StartScreen

/*
 define a Level, has standard values for basic endless game, everything can be changed to freely
 create a variety of levels
 */
class LevelDef {
    val worldWidth = 40f
    val worldHeight = 80f

    val ballsList : ArrayList<Ball> = ArrayList()
    val ballsToRemoveList : ArrayList <Ball> = ArrayList()

    val hopperList : ArrayList<Hopper> = ArrayList()

    val obstacleList : ArrayList<Obstacle> = ArrayList()

    var world : World = World(Vector2(0f,-9.8f), true)

    var gameState = GameState.STARTSCREEN
    val gameOverScreen = GameOverScreen()
    val startScreen = StartScreen()
    val pauseScreen = GamePause()
    val settingsScreen = SettingsScreen()

    val interactionMethod = InteractionMethod.INDIRECT_SWIPE


    var spawner : Spawner = Spawner(worldWidth / 2, worldHeight - 5, 3.5f,world)
    var increaseSpawnInterval = true
    var dispatcherLeft: Dispatcher = Dispatcher(world, 11f, worldHeight - 32f, DispatcherOrientation.LEFT)
    var dispatcherRight: Dispatcher = Dispatcher(world, 21f, worldHeight - 32f, DispatcherOrientation.RIGHT)
    var mover: Mover

    var ground : BallDestroyer = BallDestroyer(world, Vector2(0f, 0f), Vector2(40f, 0f), Vector2(0f, 0.1f))
    var leftBorder : Border = Border(world, 0.1f, 80f, Vector2(1f, 40f))
    var rightBorder : Border = Border(world, 0.1f, 80f, Vector2(39f, 40f))
    var objectSelectedByPlayer : MovableObjects = MovableObjects.DISPATCHER

    var soundVolume = 1f
    var gameOverSound = Gdx.audio.newSound(Gdx.files.internal("MusicAndSound/GameOverSound.mp3"))
    var ballCollisionSound = Gdx.audio.newSound(Gdx.files.internal("MusicAndSound/BallCollisionSound.mp3"))
    var scoreSound = Gdx.audio.newSound(Gdx.files.internal("MusicAndSound/ScoreSound.mp3"))
    var music = Gdx.audio.newMusic(Gdx.files.internal("MusicAndSound/MainMusic.mp3"))
    var playSound = true
    var playMusic = true
    init {
        // init standard hopper setup, can be changed
        val greenHopper = Hopper(GameColor.GREEN, world, worldWidth * 0.5f -2, 0f)
        hopperList.add(greenHopper)
        val blueHopper = Hopper(GameColor.RED, world, worldWidth * 0.2f -2, 0f)
        hopperList.add(blueHopper)
        val redHopper = Hopper(GameColor.BLUE, world, worldWidth * 0.8f -2, 0f)
        hopperList.add(redHopper)
        // init standard obstacle setup, can be changed
        val leftObstacle = Obstacle(world, worldWidth * 0.35f -2, worldHeight * 0.2f)
        obstacleList.add(leftObstacle)
        val rightObstacle = Obstacle(world, worldWidth * 0.65f -2, worldHeight * 0.2f)
        obstacleList.add(rightObstacle)
        mover = Mover(dispatcherLeft, dispatcherRight, leftObstacle, rightObstacle, 1f)
    }
}
