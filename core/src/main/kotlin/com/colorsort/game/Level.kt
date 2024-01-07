package com.colorsort.game

import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.utils.TimeUtils

class Level(levelDef: LevelDef)  {
    private val ballsList : ArrayList<Ball> = levelDef.ballsList
    private val ballsToRemoveList : ArrayList <Ball> = levelDef.ballsToRemoveList
    private var lastSpawnTime : Long = 0
    private val hopperList : ArrayList<Hopper> = levelDef.hopperList

    private val world : World = levelDef.world
    private val contactListener : ContactListener = levelDef.contactListener


    private val spawner : Spawner = levelDef.spawner
    private val dispatcherLeft: Dispatcher = levelDef.dispatcherLeft
    private val dispatcherRight: Dispatcher = levelDef.dispatcherRight
    private val dispatcherController: DispatcherController = levelDef.dispatcherController

    private val ground : DestroyingBorder = levelDef.ground
    private val leftBorder : Border = levelDef.leftBorder
    private val rightBorder : Border = levelDef.rightBorder

    fun getNextTexturePositions () : ArrayList<TextureDrawHelper> {
        // step the world
        doStep()
        // collect updated positions
        val textureDrawHelpers : ArrayList<TextureDrawHelper> = ArrayList()
        // hopper
        for (hopper in hopperList){
            textureDrawHelpers.add(TextureDrawHelper(hopper.getTexture()!!, hopper.getTexturePosition(), hopper.getTextureSize()))
        }
        // dispatcher
        textureDrawHelpers.add(TextureDrawHelper(dispatcherLeft.getTexture(), dispatcherLeft.getTexturePosition(), dispatcherLeft.getTextureSize()))
        textureDrawHelpers.add(TextureDrawHelper(dispatcherRight.getTexture(), dispatcherRight.getTexturePosition(), dispatcherRight.getTextureSize()))
        // balls
        for (ball in ballsList){
            textureDrawHelpers.add(TextureDrawHelper(ball.getTexture()!!, ball.getTexturePosition(), ball.getTextureSize()))
        }
        // spawner
        textureDrawHelpers.add(TextureDrawHelper(spawner.spawnerTexture, spawner.getTexturePosition(), spawner.getTextureSize()))

        return textureDrawHelpers
    }
    private fun doStep () {
        val currentTime : Long = TimeUtils.nanoTime()
        if (TimeUtils.timeSinceNanos(lastSpawnTime) > spawner.spawnInterval * 1_000_000_000L){
            ballsList.add(spawner.spawnBall())
            lastSpawnTime = currentTime
        }
        world.step(1/60f,6,2)
        processBallsForRemoval()
    }
    private fun processBallsForRemoval() {
        for (ball in ballsToRemoveList){
            ballsList.remove(ball)
            ball.destroyBody(world)
        }
    }
    fun dispose () {
        for (ball in ballsList){
            ball.getTexture()?.dispose()
        }
        for (hopper in hopperList){
            hopper.getTexture()?.dispose()
        }
        spawner.spawnerTexture.dispose()
        dispatcherRight.getTexture().dispose()
        dispatcherLeft.getTexture().dispose()
    }
}
