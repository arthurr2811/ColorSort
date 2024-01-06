package com.colorsort.game

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.polygon

class Dispatcher (world: World, private val positionX : Float, private val positionY : Float, orientation: DispatcherOrientation) {
    private var dispatcherBody : Body? = null
    private var dispatcherFixture : Fixture? = null
    init {
        // ToDO Fix Dispatcher Fixture Position
        if (orientation == DispatcherOrientation.LEFT){
            dispatcherBody = world.body {
                position.set(positionX, positionY)
            }
            dispatcherFixture = dispatcherBody!!.polygon(Vector2(0f, 0f), Vector2(35f, 0f), Vector2(0f, 25f)){disposeOfShape = true}
        } else if (orientation == DispatcherOrientation.RIGHT){
            dispatcherBody = world.body {
                position.set(positionX, positionY)
            }
            dispatcherFixture = dispatcherBody!!.polygon(Vector2(0f, 0f), Vector2(0f, 25f), Vector2(35f, 0f)){disposeOfShape = true}
        }
    }
    fun getDispatcherBody (): Body? {
        return this.dispatcherBody
    }
    fun getDispatcherFixture() : Fixture? {
        return this.dispatcherFixture
    }
}
