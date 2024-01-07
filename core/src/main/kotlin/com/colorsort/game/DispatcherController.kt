package com.colorsort.game

import com.badlogic.gdx.input.GestureDetector.GestureAdapter
import com.badlogic.gdx.math.Vector2

class DispatcherController(private val dispatcherLeft: Dispatcher, private val dispatcherRight: Dispatcher, private var sensitivity : Float) : GestureAdapter() {
    init {
        if (sensitivity <= 0 || sensitivity > 1){
            sensitivity = 1f
        }
    }
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        // *0.3, otherwise to big
        moveDispatcher(deltaX * 0.3f)
        return true
    }
    private fun moveDispatcher(deltaX: Float){
        // left one as input, right fixed 14f more right
        val currentPositionDispLeft = dispatcherLeft.getDispatcherBody()!!.position
        val newPositionDispLeft = Vector2(currentPositionDispLeft.x + deltaX * sensitivity, currentPositionDispLeft.y)
        val newPositionDispRight = Vector2(newPositionDispLeft.x + 14f, newPositionDispLeft.y)
        if (newPositionDispLeft.x < 1f){
            newPositionDispLeft.x = 1f
            newPositionDispRight.x = 15f
        } else if (newPositionDispLeft.x > 18){
            newPositionDispLeft.x = 18f
            newPositionDispRight.x = 32f
            }
        dispatcherLeft.getDispatcherBody()!!.setTransform(newPositionDispLeft, 0f)
        dispatcherRight.getDispatcherBody()!!.setTransform(newPositionDispRight, 0f)
    }
}
