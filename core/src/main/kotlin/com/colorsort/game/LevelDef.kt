package com.colorsort.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
// to define a Level, has standard values for basic endless game, everything can be changed
class LevelDef () {
    private val worldWidth = 40f
    private val worldHeight = 80f

    val ballsList : ArrayList<Ball> = ArrayList()
    val ballsToRemoveList : ArrayList <Ball> = ArrayList()

    val hopperList : ArrayList<Hopper> = ArrayList()

    var world : World = World(Vector2(0f,-9.8f), true)
    var contactListener : ContactListener = ContactListener(ballsList, ballsToRemoveList)


    var spawner : Spawner = Spawner(worldWidth / 2, worldHeight - 5, 2f,world)
    var dispatcherLeft: Dispatcher = Dispatcher(world, 9f, worldHeight - 32f, DispatcherOrientation.LEFT)
    var dispatcherRight: Dispatcher = Dispatcher(world, 23f, worldHeight - 32f, DispatcherOrientation.RIGHT)
    var dispatcherController: DispatcherController = DispatcherController(dispatcherLeft, dispatcherRight, 0.3f)

    var ground : DestroyingBorder = DestroyingBorder(world, Vector2(0f, 0f), Vector2(40f, 0f), Vector2(0f, 0.1f))
    var leftBorder : Border = Border(world, 0.1f, 80f, Vector2(0.05f, 40f))
    var rightBorder : Border = Border(world, 0.1f, 80f, Vector2(39.95f, 40f))
    init {
        world.setContactListener(contactListener)
        Gdx.input.inputProcessor = GestureDetector(dispatcherController)
        val greenHopper = Hopper(GameColor.GREEN, world, worldWidth * 0.5f -2, 0f)
        hopperList.add(greenHopper)
        val blueHopper = Hopper(GameColor.RED, world, worldWidth * 0.2f -2, 0f)
        hopperList.add(blueHopper)
        val redHopper = Hopper(GameColor.BLUE, world, worldWidth * 0.8f -2, 0f)
        hopperList.add(redHopper)
    }
}