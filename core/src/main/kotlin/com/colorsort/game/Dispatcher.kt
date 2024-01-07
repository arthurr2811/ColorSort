package com.colorsort.game

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.polygon

class Dispatcher (world: World, private val positionX : Float, private val positionY : Float, private val orientation: DispatcherOrientation) {
    private var dispatcherBody : Body? = null
    private var dispatcherFixture : Fixture? = null
    private val dispatcherLeftTexture by lazy { Texture("DispatcherLeft.png") }
    private val dispatcherRightTexture by lazy { Texture("DispatcherRight.png") }
    init {
        if (orientation == DispatcherOrientation.LEFT){
            dispatcherBody = world.body {
                position.set(positionX, positionY)
            }
            dispatcherFixture = dispatcherBody!!.polygon(Vector2(0f, 0f), Vector2(70f, 0f), Vector2(70f, 50f)){disposeOfShape = true}
        } else if (orientation == DispatcherOrientation.RIGHT){
            dispatcherBody = world.body {
                position.set(positionX, positionY)
            }
            dispatcherFixture = dispatcherBody!!.polygon(Vector2(0f, 0f), Vector2(0f, 50f), Vector2(70f, 0f)){disposeOfShape = true}
        }
    }
    fun getDispatcherBody (): Body? {
        return this.dispatcherBody
    }
    fun getTexturePosition () : Vector2 {
        return Vector2(dispatcherBody!!.position.x, dispatcherBody!!.position.y)
    }
    fun getDispatcherFixture() : Fixture? {
        return this.dispatcherFixture
    }
    fun getTexture () : Texture {
        return if (orientation == DispatcherOrientation.RIGHT){
            dispatcherRightTexture
        } else {
            dispatcherLeftTexture
        }
    }
}
