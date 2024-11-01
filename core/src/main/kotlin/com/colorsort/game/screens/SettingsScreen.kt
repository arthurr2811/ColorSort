package com.colorsort.game.screens

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
        // ToDO je nachdem wo entspr. auslösen: reset Highscore oder set interactiopnMEthod
    }
    private fun resetHighScore(){
        level.setHighScore(0)
    }
    private fun setInteractionMethod(interactionMethod: InteractionMethod){
        level.setInteractionMethod(interactionMethod)
    }
}
