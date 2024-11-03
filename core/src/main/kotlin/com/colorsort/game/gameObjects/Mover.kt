package com.colorsort.game.gameObjects

import com.badlogic.gdx.math.Vector2
/*
maps the input fro the player to movement of the dispatcher
 */
class Mover(private val dispatcherLeft: Dispatcher, private val dispatcherRight: Dispatcher,
            private val obstacleLeft: Obstacle, private val obstacleRight: Obstacle, private var sensitivity : Float) {
    init {
        if (sensitivity <= 0 || sensitivity > 1){
            sensitivity = 1f
        }
    }
    fun moveDispatcher(deltaX: Float){
        // we only move the left dispatcher, the right one is just fixed 10f more to the right
        val currentPositionDispLeft = dispatcherLeft.getBody()!!.position
        val newPositionDispLeft = Vector2(currentPositionDispLeft.x + deltaX * sensitivity, currentPositionDispLeft.y)
        val newPositionDispRight = Vector2(newPositionDispLeft.x + 10f, newPositionDispLeft.y)
        // make sure dispatcher is not moved out of game
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
    fun moveObstacles(deltaX: Float){
        // we only move the left obstacle, the right one is just fixed 10f more to the right
        val currentPositionObstLeft = obstacleLeft.getBody()!!.position
        val newPositionObstLeft = Vector2(currentPositionObstLeft.x + deltaX * sensitivity, currentPositionObstLeft.y)
        val newPositionObstRight = Vector2(newPositionObstLeft.x + 12f, newPositionObstLeft.y)
        // make sure obstacle is not moved out of game
        if (newPositionObstLeft.x < 1f){
            newPositionObstLeft.x = 1f
            newPositionObstRight.x = 13f
        } else if (newPositionObstLeft.x > 22){
            newPositionObstLeft.x = 22f
            newPositionObstRight.x = 34f
        }
        obstacleLeft.getBody()!!.setTransform(newPositionObstLeft, 0f)
        obstacleRight.getBody()!!.setTransform(newPositionObstRight, 0f)
    }
    // to center the dispatcher and obstacles after game over
    fun center() {
        dispatcherLeft.getBody()!!.setTransform(Vector2(11f, 48f), 0f)
        dispatcherRight.getBody()!!.setTransform(Vector2(21f, 48f), 0f)
        obstacleLeft.getBody()!!.setTransform(Vector2(12f, 16f), 0f)
        obstacleRight.getBody()!!.setTransform(Vector2(24f, 16f), 0f)
    }
    fun getDispatcherPosition() : Vector2{
        // returns center of current dispatcher in the world
        val positionLeftDispatcher = dispatcherLeft.getBody()!!.position
        return Vector2(positionLeftDispatcher.x + 9f, positionLeftDispatcher.y -18f)
    }
    fun getObstaclePosition() : Vector2{
        // returns center of current dispatcher in the world
        val positionLeftObstacle = obstacleLeft.getBody()!!.position
        return Vector2(positionLeftObstacle.x + 9f, 78f - positionLeftObstacle.y)
    }
}
