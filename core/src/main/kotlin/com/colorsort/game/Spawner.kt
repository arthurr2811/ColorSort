package com.colorsort.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
import kotlin.random.Random

class Spawner (private val spawnPointX : Float, private val spawnPointY : Float, val spawnInterval : Float, private val world: World) {
    val spawnerTexture by lazy { Texture("Spawner160_240.png") }
    fun spawnBall (color: GameColor = GameColor.RANDOM) : Ball{
        val realColor = if (color == GameColor.RANDOM) {
            GameColor.entries[Random.nextInt(GameColor.entries.size - 1)]
        } else {
            color
        }
        return Ball(realColor, world, spawnPointX, spawnPointY)
    }
    fun getTexturePosition () : Vector2 {
        return Vector2(spawnPointX -2, spawnPointY - 1)
    }
    fun getTextureSize() : Vector2 {
        return Vector2(4f,6f)
    }
}
