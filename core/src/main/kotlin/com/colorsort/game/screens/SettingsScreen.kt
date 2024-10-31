package com.colorsort.game.screens

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.colorsort.game.helpers.TextureDrawHelper

class SettingsScreen {
    private val settingsScreenTexture by lazy { Texture("Screens/SettingsScreen400_800.png") }
    fun getTexturePositions () : ArrayList<TextureDrawHelper> {
        val textureDrawHelpers = ArrayList<TextureDrawHelper>()
        textureDrawHelpers.add(TextureDrawHelper(settingsScreenTexture, Vector2(0f, 0f), Vector2(40f, 80f)))
        return textureDrawHelpers
    }
}
