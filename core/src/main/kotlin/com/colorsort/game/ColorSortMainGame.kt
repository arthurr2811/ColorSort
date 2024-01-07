package com.colorsort.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class ColorSortMainGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    // Game Objects textures ToDO move to coreesponding classes

    //private val obstacleTexture by lazy { Texture("Obstacle.png") }

    // Camera
    private val camera : OrthographicCamera = OrthographicCamera()
    // balls list with physics
    /*
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
    private lateinit var ground : DestroyingBorder
    private lateinit var leftBorder : Border
    private lateinit var rightBorder : Border

     */


    private lateinit var endlessMode : Level
    /*
    ToDo: add scaling
    ToDo: add score add highscore
    Ideas for later: start screen, pause button and menue, adjustable gamerule (spawnspeed etc.)
     */

    override fun create() {
        // camera
        val screenX = 400f
        val screenY = 800f
        camera.setToOrtho(false, screenX, screenY)
        // init endless mode
        val levelDef = LevelDef()
        endlessMode = Level(levelDef)
    }

    override fun render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        batch.projectionMatrix = camera.combined
        batch.begin()
        for (texturePosition in endlessMode.getNextTexturePositions()){
            batch.draw(texturePosition.texture, texturePosition.position.x, texturePosition.position.y)
        }
        batch.end()
    }
    override fun dispose() {
        endlessMode.dispose()
    }
}
