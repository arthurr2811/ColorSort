package com.colorsort.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.colorsort.game.helpers.InputHandler
import com.colorsort.game.levels.InteractionMethod
import com.colorsort.game.levels.Level
import com.colorsort.game.levels.LevelDef
import com.colorsort.game.screens.GameState
/*
the technical part of the application, handles everything not directly game related, like
scaling the game world to the screen size, persist settings and highScore, rendering the game
to the screen, android life cycle
 */
/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class ColorSortMainGame : ApplicationAdapter() {
    // camera and render
    private val camera : OrthographicCamera = OrthographicCamera()
    private val batch by lazy { SpriteBatch() }
    // world size, screen size, scaling
    private var screenWidth = 0f
    private var screenHeight = 0f
    private val worldWidth = 40f
    private val worldHeight = 80f
    private lateinit var viewport: Viewport
    private var scaleFactorX = 0f
    private var scaleFactorY = 0f
    // endless mode
    private lateinit var endlessMode : Level
    // font
    private lateinit var font : BitmapFont
    // debug render
   // private val debugRenderer by lazy { Box2DDebugRenderer() }
    // input handler
    private lateinit var inputHandler: InputHandler
    // shared preferences to persist high score
    private lateinit var preferences : Preferences

    /*
    Ideas for later: adjustable game rules,
                     implement different levels (not endless, Level class boolean endless)
                     implement in game currency and skins (collect coins by playing,
                     buy skins, new levels etc. with coins)
     */

    // init everything
    override fun create() {
        // camera
        viewport = FitViewport(worldWidth, worldHeight, camera)
        // init endless mode
        val levelDef = LevelDef()
        endlessMode = Level(levelDef)
        // calculate scale factors (our world is 40x80,the devices screen has different solution)
        screenWidth = Gdx.graphics.width.toFloat()
        screenHeight = Gdx.graphics.height.toFloat()
        scaleFactorX = screenWidth / worldWidth
        scaleFactorY = screenHeight / worldHeight
        // set projection to screen size
        batch.projectionMatrix = Matrix4().setToOrtho2D(0f, 0f, screenWidth, screenHeight)
        // font
        font = BitmapFont()
        // input handler
        inputHandler = InputHandler(endlessMode)
        Gdx.input.inputProcessor = GestureDetector(inputHandler)
        // load persistent preferences from shared preferences
        preferences = Gdx.app.getPreferences("ColorSortPreferences")
        endlessMode.setHighScore(preferences.getInteger("highscore", 0))
        endlessMode.soundOfOrOn(preferences.getBoolean("sound", true))
        endlessMode.musicOfOrOn(preferences.getBoolean("music", true))
        endlessMode.setInteractionMethod(InteractionMethod.fromInt(
            preferences.getInteger("interactionMethod", 1))?: InteractionMethod.DIRECT)
    }
    // sizes camera to screen size
    override fun resize(width: Int, height: Int) {
        viewport.update(width, height,true)
    }
    // render everything that needs to be rendered in the current frame
    override fun render() {
        // set background color to light grey
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        // update camera
        camera.update()
        // debug renderer
        // debugRenderer.render(endlessMode.getWorld(), camera.combined)
        // draw everything on batch
        batch.begin()
        for (texturePosition in endlessMode.getNextTexturePositions()){
            batch.draw(texturePosition.texture, texturePosition.position.x * scaleFactorX, texturePosition.position.y * scaleFactorY,
                texturePosition.dimensions.x * scaleFactorX, texturePosition.dimensions.y * scaleFactorY)
        }
        for (text in endlessMode.getTexts()){
            font.color = text.color
            font.data.setScale(text.scale)
            font.draw(batch, text.text, text.position.x * scaleFactorX, text.position.y * scaleFactorY, 0f, Align.center, false)
        }
        batch.end()
    }
    // pause game, when app is put to pause by android, save preferences
    override fun pause() {
        savePreferences()
        if(endlessMode.gameState == GameState.INGAME){
            endlessMode.gameState = GameState.PAUSED
        }
    }
    // before app gets killed save preferences and dispose textures
    override fun dispose() {
        savePreferences()
        // dispose textures
        endlessMode.dispose()
    }
    private fun savePreferences() {
        preferences.putInteger("highscore", endlessMode.getHighScore())
        preferences.putBoolean("sound", endlessMode.isSoundOn())
        preferences.putBoolean("music", endlessMode.isMusicOn())
        preferences.putInteger("interactionMethod", endlessMode.getInteractionMethod().toInt())
        preferences.flush()
    }
}
