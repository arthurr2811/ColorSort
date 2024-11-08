package com.colorsort.game.screens
/*
in the settings screen its possible to reset the highScore and switch controls (aka interactionMethods)
 */
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.colorsort.game.helpers.TextureDrawHelper
import com.colorsort.game.levels.InteractionMethod
import com.colorsort.game.levels.Level

class SettingsScreen() {
    private val settingsScreenTexture by lazy { Texture("Screens/SettingsScreen400_800.png") }
    private val controlPanelTexture by lazy { Texture("Screens/controlPanel200_500.png") }

    private val resetHighscoreTexture by lazy { Texture("ButtonsAndIcons/ResetHighscore20_5.png") }
    private val directTexture by lazy { Texture("ButtonsAndIcons/direct15_5.png") }
    private val directSelectTexture by lazy { Texture("ButtonsAndIcons/directSelect15_5.png") }
    private val indirectSwipeTexture by lazy { Texture("ButtonsAndIcons/indirectSwipe15_5.png") }
    private val indirectSwipeSelectTexture by lazy { Texture("ButtonsAndIcons/indirectSwipeSelect15_5.png") }
    private val indirectTapTexture by lazy { Texture("ButtonsAndIcons/indirectTap15_5.png") }
    private val indirectTapSelectTexture by lazy { Texture("ButtonsAndIcons/indirectTapSelect15_5.png") }
    lateinit var level : Level

    fun getTexturePositions (interactionMethod: InteractionMethod) : ArrayList<TextureDrawHelper> {
        val textureDrawHelpers = ArrayList<TextureDrawHelper>()
        // add everything general
        textureDrawHelpers.add(TextureDrawHelper(settingsScreenTexture, Vector2(0f, 0f), Vector2(40f, 80f)))
        textureDrawHelpers.add(TextureDrawHelper(controlPanelTexture, Vector2(10f, 20f), Vector2(20f, 50f)))
        textureDrawHelpers.add(TextureDrawHelper(resetHighscoreTexture, Vector2(10f, 10f), Vector2(20f, 5f)))
        // buttons depending on current selected interaction method
        when(interactionMethod){
            InteractionMethod.DIRECT -> {
                textureDrawHelpers.add(TextureDrawHelper(directSelectTexture, Vector2(12.5f, 50f), Vector2(15f, 5f)))
                textureDrawHelpers.add(TextureDrawHelper(indirectTapTexture, Vector2(12.5f, 40f), Vector2(15f, 5f)))
                textureDrawHelpers.add(TextureDrawHelper(indirectSwipeTexture, Vector2(12.5f, 30f), Vector2(15f, 5f)))
            }
            InteractionMethod.INDIRECT_TAP -> {
                textureDrawHelpers.add(TextureDrawHelper(directTexture, Vector2(12.5f, 50f), Vector2(15f, 5f)))
                textureDrawHelpers.add(TextureDrawHelper(indirectTapSelectTexture, Vector2(12.5f, 40f), Vector2(15f, 5f)))
                textureDrawHelpers.add(TextureDrawHelper(indirectSwipeTexture, Vector2(12.5f, 30f), Vector2(15f, 5f)))
            }
            InteractionMethod.INDIRECT_SWIPE -> {
                textureDrawHelpers.add(TextureDrawHelper(directTexture, Vector2(12.5f, 50f), Vector2(15f, 5f)))
                textureDrawHelpers.add(TextureDrawHelper(indirectTapTexture, Vector2(12.5f, 40f), Vector2(15f, 5f)))
                textureDrawHelpers.add(TextureDrawHelper(indirectSwipeSelectTexture, Vector2(12.5f, 30f), Vector2(15f, 5f)))
            }

        }



        return textureDrawHelpers
    }
    fun handleTouchInput(x: Float, y: Float){
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        // if tap on location of reset highScore button --> reset highScore
        if (x > screenWidth * 0.25 && x < screenWidth * 0.75
            && y > screenHeight * 0.8 && y < screenHeight * 0.85){
            resetHighScore()
            return
        }
        // same for all interaction method buttons
        if (x > screenWidth * 0.3 && x < screenWidth * 0.7){
            if (y > screenHeight * 0.32 && y < screenHeight * 0.37){
                setInteractionMethod(InteractionMethod.DIRECT)
                return
            }
            if (y > screenHeight * 0.45 && y < screenHeight * 0.5){
                setInteractionMethod(InteractionMethod.INDIRECT_TAP)
                return
            }
            if (y > screenHeight * 0.57 && y < screenHeight * 0.62){
                setInteractionMethod(InteractionMethod.INDIRECT_SWIPE)
                return
            }
        }
    }
    private fun resetHighScore(){
        level.setHighScore(0)
    }
    private fun setInteractionMethod(interactionMethod: InteractionMethod){
        level.setInteractionMethod(interactionMethod)
    }
}
