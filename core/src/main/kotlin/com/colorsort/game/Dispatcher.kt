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
    private val dispatcherLeftTexture by lazy { Texture("DispatcherL280_200.png") }
    private val dispatcherRightTexture by lazy { Texture("DispatcherR280_200.png") }
    init {
        if (orientation == DispatcherOrientation.LEFT){
            dispatcherBody = world.body {
                position.set(positionX, positionY)
            }
            dispatcherFixture = dispatcherBody!!.polygon(Vector2(0f, 0f), Vector2(7f, 0f), Vector2(7f, 5f)){disposeOfShape = true}
        } else if (orientation == DispatcherOrientation.RIGHT){
            dispatcherBody = world.body {
                position.set(positionX, positionY)
            }
            dispatcherFixture = dispatcherBody!!.polygon(Vector2(0f, 0f), Vector2(0f, 5f), Vector2(7f, 0f)){disposeOfShape = true}
        }
    }
    fun getDispatcherBody (): Body? {
        return this.dispatcherBody
    }
    fun getTexturePosition () : Vector2 {
        return Vector2(dispatcherBody!!.position.x - (7 * (1/3)), dispatcherBody!!.position.y- (5 * (1/3)))
    }
    fun getTextureSize() : Vector2 {
        return Vector2(7f,5f)
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