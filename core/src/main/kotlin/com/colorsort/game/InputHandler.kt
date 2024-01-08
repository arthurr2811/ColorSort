package com.colorsort.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.input.GestureDetector

class InputHandler (val level: Level) : GestureDetector.GestureAdapter() {
    val screenWidth = Gdx.graphics.width.toFloat()
    val screenHeight = Gdx.graphics.height.toFloat()
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        if (level.gameState == GameState.INGAME){
            // *0.3 otherwise movements to large
            level.dispatcherController.moveDispatcher(deltaX * 0.3f)
        }
        return true
    }

    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        if (level.gameState == GameState.INGAME && x > (screenWidth - screenWidth * 0.1) && y < (screenHeight - screenHeight * 0.1)){
            level.gameState = GameState.PAUSED
            return true
        }
        if (level.gameState == GameState.STARTSCREEN || level.gameState == GameState.GAMEOVER || level.gameState == GameState.PAUSED){
            level.gameState = GameState.INGAME
            return true
        }
        return true
    }
}
