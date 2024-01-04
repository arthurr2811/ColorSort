package com.colorsort.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms. */
class ColorSort : ApplicationAdapter() {
    private val batch by lazy { SpriteBatch() }
    private val image by lazy { Texture("testBall.png") }

    override fun render() {
        //ball textur testweise mittig oben platzieren
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        // dazu coords ausrechnen
        val x = (Gdx.graphics.width - image.width) / 2f
        val totalHeight = Gdx.graphics.height
        val y = totalHeight * 0.9f - image.height // 10% der absoluten höhe hoch anetzen
        batch.begin()
        batch.draw(image, x, y)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        image.dispose()
    }
}
