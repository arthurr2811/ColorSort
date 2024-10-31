package com.colorsort.game.screens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.colorsort.game.helpers.TextureDrawHelper
import com.colorsort.game.levels.InteractionMethod
import com.colorsort.game.levels.Level

class SettingsScreen() {
    private val settingsScreenTexture by lazy { Texture("Screens/SettingsScreen400_800.png") }
    lateinit var level : Level

    // ToDO buttons für resetHighScore und die 3 Steuerungsmethoden

    fun getTexturePositions () : ArrayList<TextureDrawHelper> {
        val textureDrawHelpers = ArrayList<TextureDrawHelper>()
        textureDrawHelpers.add(TextureDrawHelper(settingsScreenTexture, Vector2(0f, 0f), Vector2(40f, 80f)))
        // ToDO Button reset highscore, buttons steuerungsvarianten 1, 2, 3
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
