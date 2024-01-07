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

    fun getNextTexturePositions () : ArrayList<TexturePosition> {
        // step the world
        doStep()
        // collect updated positions
        val nextTexturePositions : ArrayList<TexturePosition> = ArrayList()
        // hopper
        for (hopper in hopperList){
            nextTexturePositions.add(TexturePosition(hopper.getTexture()!!, hopper.getTexturePosition()))
        }
        // dispatcher
        nextTexturePositions.add(TexturePosition(dispatcherLeft.getTexture(), dispatcherLeft.getTexturePosition()))
        nextTexturePositions.add(TexturePosition(dispatcherRight.getTexture(), dispatcherRight.getTexturePosition()))
        // balls
        for (ball in ballsList){
            nextTexturePositions.add(TexturePosition(ball.getTexture()!!, ball.getTexturePosition()))
        }
        // spawner
        nextTexturePositions.add(TexturePosition(spawner.spawnerTexture, spawner.getTexturePosition()))

        return nextTexturePositions
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
