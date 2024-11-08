package com.colorsort.game.gameObjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import kotlin.random.Random
/*
spawns the balls. Has a spawn point, spawn interval and exists in a world (box2D). spawn interval is
var so it can be changed in game to increase difficulty
 */
class Spawner (private val spawnPointX : Float, private val spawnPointY : Float, var spawnInterval : Float, private val world: World) :
    GameObject {
    val defaultSpawnInterval = spawnInterval
    // texture
    private val spawnerTexture by lazy { Texture("Spawner160_240.png") }
    fun spawnBall (color: GameColor = GameColor.RANDOM) : Ball {
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
