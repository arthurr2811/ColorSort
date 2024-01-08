package com.colorsort.game

import com.badlogic.gdx.input.GestureDetector.GestureAdapter
import com.badlogic.gdx.math.Vector2
// map the player input to dispatcher movement
class DispatcherController(private val dispatcherLeft: Dispatcher, private val dispatcherRight: Dispatcher, private var sensitivity : Float) : GestureAdapter() {
    init {
        if (sensitivity <= 0 || sensitivity > 1){
            sensitivity = 1f
        }
    }
    // get player input
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        // *0.3 otherwise movements to large
        moveDispatcher(deltaX * 0.3f)
        return true
    }
    private fun moveDispatcher(deltaX: Float){
        // only move left one, right one is just fixed 10f more to the right
        val currentPositionDispLeft = dispatcherLeft.getBody()!!.position
        val newPositionDispLeft = Vector2(currentPositionDispLeft.x + deltaX * sensitivity, currentPositionDispLeft.y)
        val newPositionDispRight = Vector2(newPositionDispLeft.x + 10f, newPositionDispLeft.y)
        // make sure dispatcher is not moved to far
        if (newPositionDispLeft.x < 1f){
            newPositionDispLeft.x = 1f
            newPositionDispRight.x = 11f
        } else if (newPositionDispLeft.x > 22){
            newPositionDispLeft.x = 22f
            newPositionDispRight.x = 32f
            }
        dispatcherLeft.getBody()!!.setTransform(newPositionDispLeft, 0f)
        dispatcherRight.getBody()!!.setTransform(newPositionDispRight, 0f)
    }
}
