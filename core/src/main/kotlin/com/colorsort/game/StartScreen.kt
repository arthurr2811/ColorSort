package com.colorsort.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
// screen shown at start of game
class StartScreen {
    private val startScreenTexture by lazy { Texture("StartScreen400_800.png") }
    private val soundOnTexture by lazy { Texture("SoundOn240_240.png") }
    private val soundOffTexture by lazy { Texture("SoundOff240_240.png") }
    private val musicOnTexture by lazy { Texture("MusicOn240_240.png") }
    private val musicOffTexture by lazy { Texture("MusicOff240_240.png") }

    fun getTexturePositions (sound : Boolean, music : Boolean) : ArrayList<TextureDrawHelper> {
        val textureDrawHelpers = ArrayList<TextureDrawHelper>()
        textureDrawHelpers.add(TextureDrawHelper(startScreenTexture, Vector2(0f, 0f), Vector2(40f, 80f)))
        if (sound){
            textureDrawHelpers.add(TextureDrawHelper(soundOnTexture, Vector2(7.5f, 10f), Vector2(5f,5f)))
        } else {
            textureDrawHelpers.add(TextureDrawHelper(soundOffTexture, Vector2(7.5f, 10f), Vector2(5f,5f)))
        }
        if (music){
            textureDrawHelpers.add(TextureDrawHelper(musicOnTexture, Vector2(27.5f, 10f), Vector2(5f,5f)))
        } else {
            textureDrawHelpers.add(TextureDrawHelper(musicOffTexture, Vector2(27.5f, 10f), Vector2(5f,5f)))
        }
        return textureDrawHelpers
    }
}
