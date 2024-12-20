package com.colorsort.game.gameObjects

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import ktx.box2d.body
import ktx.box2d.polygon
/*
a double triangle shape. Moved by the player to influence the balls. The dispatcher is build from
two triangles which are mirrored. Therefore orientation tells the two triangles apart. It has a
position and exists in a world (box2D). It can be selected or not selected.
 */
class Dispatcher (world: World, private val positionX : Float, private val positionY : Float, private val orientation: DispatcherOrientation) :
    GameObject {
    private var dispatcherBody : Body? = null
    private var dispatcherFixture : Fixture? = null
    private var selected : Boolean = true
    private val dispatcherLeftSelectTexture by lazy { Texture("Dispatcher/DispatcherLSelect280_200.png") }
    private val dispatcherRightSelectTexture by lazy { Texture("Dispatcher/DispatcherRSelect280_200.png") }
    private val dispatcherLeftTexture by lazy { Texture("Dispatcher/DispatcherL280_200.png") }
    private val dispatcherRightTexture by lazy { Texture("Dispatcher/DispatcherR280_200.png") }
    init {
        if (orientation == DispatcherOrientation.LEFT){
            dispatcherBody = world.body {
                position.set(positionX, positionY)
            }
            // triangle pointing to the left
            dispatcherFixture = dispatcherBody!!.polygon(Vector2(0f, 0f), Vector2(7f, 0f), Vector2(7f, 5f)){disposeOfShape = true}
        } else if (orientation == DispatcherOrientation.RIGHT){
            dispatcherBody = world.body {
                position.set(positionX, positionY)
            }
            // triangle pointing to the right
            dispatcherFixture = dispatcherBody!!.polygon(Vector2(0f, 0f), Vector2(0f, 5f), Vector2(7f, 0f)){disposeOfShape = true}
        }
    }
    override fun getTexture () : Texture {
        return if (selected){
            if (orientation == DispatcherOrientation.RIGHT){
                dispatcherRightSelectTexture
            } else {
                dispatcherLeftSelectTexture
            }
        } else {
            if (orientation == DispatcherOrientation.RIGHT){
                dispatcherRightTexture
            } else {
                dispatcherLeftTexture
            }
        }
    }
    // the texture is rendered from bottom left, the body position is in center of body
    // so we need to offset the texture. A rectangular triangle has its center 1/3 its height and length
    override fun getTexturePosition () : Vector2 {
        return Vector2(dispatcherBody!!.position.x - (7 * (1/3)), dispatcherBody!!.position.y- (5 * (1/3)))
    }
    override fun getTextureSize() : Vector2 {
        return Vector2(7f,5f)
    }
    override fun getBody (): Body? {
        return this.dispatcherBody
    }
    override fun getFixture() : Fixture? {
        return this.dispatcherFixture
    }
    fun select(){
        selected = true
    }
    fun unSelect(){
        selected = false
    }
}
