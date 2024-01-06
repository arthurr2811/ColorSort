package com.colorsort.game

import com.badlogic.gdx.input.GestureDetector.GestureAdapter
import com.badlogic.gdx.math.Vector2

class DispatcherController(private val dispatcherLeft: Dispatcher, private val dispatcherRight: Dispatcher) : GestureAdapter() {
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        moveDispatcher(deltaX)
        return true
    }
    private fun moveDispatcher(deltaX: Float){
        // left one as input, right fixed 140f more right
        val currentPositionDispLeft = dispatcherLeft.getDispatcherBody()!!.position
        val newPositionDispLeft = Vector2(currentPositionDispLeft.x + deltaX, currentPositionDispLeft.y)
        val newPositionDispRight = Vector2(newPositionDispLeft.x + 140f, newPositionDispLeft.y)
        if (newPositionDispLeft.x < 10f){
            newPositionDispLeft.x = 10f
            newPositionDispRight.x = 150f
        } else if (newPositionDispLeft.x > 180f){
            newPositionDispLeft.x = 180f
            newPositionDispRight.x = 320f
            }
        dispatcherLeft.getDispatcherBody()!!.setTransform(newPositionDispLeft, 0f)
        dispatcherRight.getDispatcherBody()!!.setTransform(newPositionDispRight, 0f)
    }
}
