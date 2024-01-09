package com.colorsort.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.utils.TimeUtils

class InputHandler (val level: Level) : GestureDetector.GestureAdapter() {
    var timeStampPaused : Long = 0
    val screenWidth = Gdx.graphics.width.toFloat()
    val screenHeight = Gdx.graphics.height.toFloat()
    // if pan gesture and game state = in game -> move dispatcher
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        if (level.gameState == GameState.INGAME){
            // *0.3 otherwise movements to large
            level.dispatcherController.moveDispatcher(deltaX * 0.3f)
        }
        return true
    }
    // process tap gesture corresponding to game state
    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
        if (level.gameState == GameState.INGAME && x > (screenWidth - screenWidth * 0.1) && y < (screenHeight - screenHeight * 0.1)){
            timeStampPaused = TimeUtils.nanoTime()
            level.gameState = GameState.PAUSED
            return true
        }
        if (level.gameState == GameState.STARTSCREEN || level.gameState == GameState.GAMEOVER || level.gameState == GameState.PAUSED){
            if (level.gameState != GameState.PAUSED){
                level.updateScore(- level.getScore())
                level.dispatcherController.center()
            }
            if (level.gameState == GameState.PAUSED){
                // while game paused lastSpawnTime is not updated, but TimeUtils.nanoTime moves on so
                // we need to manually update lastSpawnTime
                val timeAmountPaused : Long = TimeUtils.nanoTime() - timeStampPaused
                val remainder : Long = timeAmountPaused % (level.spawner.spawnInterval * 1_000_000_000L).toLong()
                level.lastSpawnTime = TimeUtils.nanoTime() - remainder
            }
            level.gameState = GameState.INGAME
            return true
        }
        return true
    }
}
