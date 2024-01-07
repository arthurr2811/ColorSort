package com.colorsort.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class ColorSortMainGame : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    // Camera
    private val camera : OrthographicCamera = OrthographicCamera()
    private lateinit var endlessMode : Level
    // world size and screen size
    private var screenWidth = 0f
    private var screenHeight = 0f
    private val worldWidth = 40f
    private val worldHeight = 80f
    private lateinit var viewport: Viewport
    private var scaleFactorX = 0f
    private var scaleFactorY = 0f
    // debug render
    private val debugRenderer by lazy { Box2DDebugRenderer() }

    /*
    ToDo: cleanup code
    ToDo: Implement obstacle (should be same as dispatcher)
    ToDO: find right physics settings for default endless mode (dispatcher closer together?)
    ToDo: add score add highscore
    Ideas for later: start screen, pause button and menue, adjustable gamerules,
                     implement different levels (not endless, Level class boolean endless)
     */

    override fun create() {
        // camera
        viewport = FitViewport(worldWidth, worldHeight, camera)
        // init endless mode
        val levelDef = LevelDef()
        endlessMode = Level(levelDef)
        // set scale factors
        screenWidth = Gdx.graphics.width.toFloat()
        screenHeight = Gdx.graphics.height.toFloat()
        scaleFactorX = screenWidth / worldWidth
        scaleFactorY = screenHeight / worldHeight
        println(scaleFactorX)
        println(scaleFactorY)

        // projection to screen size
        batch.projectionMatrix = Matrix4().setToOrtho2D(0f, 0f, screenWidth, screenHeight)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height,true)
    }

    override fun render() {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        // debug renderer
        //debugRenderer.render(endlessMode.getWorld(), camera.combined)
        batch.begin()
        for (texturePosition in endlessMode.getNextTexturePositions()){
            batch.draw(texturePosition.texture, texturePosition.position.x * scaleFactorX, texturePosition.position.y * scaleFactorY, texturePosition.dimensions.x * scaleFactorX, texturePosition.dimensions.y * scaleFactorY) // texturewidth und texture height * scalfactorx sclafactor y
        }
        batch.end()
    }
    override fun dispose() {
        endlessMode.dispose()
    }
}
