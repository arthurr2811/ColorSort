package com.colorsort.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import kotlin.random.Random
// where the balls come from
class Spawner (private val spawnPointX : Float, private val spawnPointY : Float, var spawnInterval : Float, private val world: World) : GameObject {
    // texture
    private val spawnerTexture by lazy { Texture("Spawner160_240.png") }
    fun spawnBall (color: GameColor = GameColor.RANDOM) : Ball{
        val realColor = if (color == GameColor.RANDOM) {
            GameColor.entries[Random.nextInt(GameColor.entries.size - 1)]
        } else {
            color
        }
        return Ball(realColor, world, spawnPointX, spawnPointY)
    }

    override fun getTexture(): Texture {
        return this.spawnerTexture
    }
    // offset so texture lays over the spawn point of balls
    override fun getTexturePosition () : Vector2 {
        return Vector2(spawnPointX -2, spawnPointY - 1)
    }
    override fun getTextureSize() : Vector2 {
        return Vector2(4f,6f)
    }

    override fun getBody(): Body? {
        return null
    }

    override fun getFixture(): Fixture? {
        return null
    }
}
