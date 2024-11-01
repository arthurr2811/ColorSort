package com.colorsort.game.helpers

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.utils.TimeUtils
import com.colorsort.game.levels.Level
import com.colorsort.game.screens.GameState
/*
handles player input
 */
class InputHandler (val level: Level) : GestureDetector.GestureAdapter() {
    var timeStampPaused : Long = 0
    val screenWidth = Gdx.graphics.width.toFloat()
    val screenHeight = Gdx.graphics.height.toFloat()
    // scale input to world lengths
    val scaleFactorX = level.worldWidth / screenWidth
    val scaleFactorY = level.worldHeight / screenHeight
    // if pan gesture and game state = in game -> move dispatcher
    override fun pan(x: Float, y: Float, deltaX: Float, deltaY: Float): Boolean {
        // hand input over to level, scale to world dimensions
        if (level.gameState == GameState.INGAME){
            level.handlePlayerInput(x * scaleFactorX, y * scaleFactorY,
                deltaX * scaleFactorX, deltaY * scaleFactorY)
        }
        // if pan gesture in game over or settings screen: back to start screen
        // delay to avoid input player meant to do in game is caught here
        if(level.gameState == GameState.GAMEOVER && TimeUtils.nanoTime() > level.lastSpawnTime + 1_000_000_000
            || level.gameState == GameState.SETTINGS){
            level.gameState = GameState.STARTSCREEN
        }
        return true
    }
    // process tap gesture corresponding to game state
    override fun tap(x: Float, y: Float, count: Int, button: Int): Boolean {
       // if tap in top right corner and in game: set to pause game
        if (level.gameState == GameState.INGAME && x > (screenWidth - screenWidth * 0.1) && y < (screenHeight * 0.1)){
            timeStampPaused = TimeUtils.nanoTime()
            level.gameState = GameState.PAUSED
            return true
            // else hand input over to level
        } else if(level.gameState == GameState.INGAME){
            level.handlePlayerInput(x * scaleFactorX, y * scaleFactorY, null, null)
            return true
        }
        // if start screen and tap on sound icon change playSound
        if (level.gameState == GameState.STARTSCREEN &&
            x > (screenWidth - screenWidth * 0.8) && x < (screenWidth - screenWidth * 0.7) &&
            y > (screenHeight - screenHeight * 0.25) && y < (screenHeight - screenHeight * 0.15)){
            level.soundOfOrOn()
            return true
        }
        // same for music icon
        if (level.gameState == GameState.STARTSCREEN &&
            x > (screenWidth - screenWidth * 0.3) && x < (screenWidth - screenWidth * 0.2) &&
            y > (screenHeight - screenHeight * 0.25) && y < (screenHeight - screenHeight * 0.15)){
            level.musicOfOrOn()
            return true
        }
        // tap on settings icon, go to settings
        if (level.gameState == GameState.STARTSCREEN && x < (screenWidth * 0.18) && y < (screenHeight * 0.15)){
            level.gameState = GameState.SETTINGS
        }
        // if tap anywhere and in settings: delegate to settings input handling
        if (level.gameState == GameState.SETTINGS){
            level.inputToSettingsMenu(x, y)
        }
        // if tap anywhere and not in game: set to in game
        if (level.gameState == GameState.STARTSCREEN || level.gameState == GameState.GAMEOVER || level.gameState == GameState.PAUSED){
            // if not paused before also reset score and dispatcher
            if (level.gameState != GameState.PAUSED){
                level.resetScore()
                level.dispatcherController.center()
            }
            // if paused before: set to in game
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
