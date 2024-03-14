package com.colorsort.game.gameObjects

import com.badlogic.gdx.math.Vector2
// map the player input to dispatcher movement
class DispatcherController(private val dispatcherLeft: Dispatcher, private val dispatcherRight: Dispatcher, private var sensitivity : Float) {
    init {
        if (sensitivity <= 0 || sensitivity > 1){
            sensitivity = 1f
        }
    }
    fun moveDispatcher(deltaX: Float){
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
    fun center() {
        dispatcherLeft.getBody()!!.setTransform(Vector2(11f, 48f), 0f)
        dispatcherRight.getBody()!!.setTransform(Vector2(21f, 48f), 0f)
    }
}