package com.colorsort.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.input.GestureDetector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.World
// to define a Level, has standard values for basic endless game, everything can be changed
class LevelDef () {
    private val screenX = 400f
    private val screenY = 800f

    val ballsList : ArrayList<Ball> = ArrayList()
    val ballsToRemoveList : ArrayList <Ball> = ArrayList()

    val hopperList : ArrayList<Hopper> = ArrayList()

    var world : World = World(Vector2(0f,-10f), true)
    var contactListener : ContactListener = ContactListener(ballsList, ballsToRemoveList)


    var spawner : Spawner = Spawner(screenX / 2, screenY - 60, 2f,world)
    var dispatcherLeft: Dispatcher = Dispatcher(world, 95f, screenY - 325f, DispatcherOrientation.LEFT)
    var dispatcherRight: Dispatcher = Dispatcher(world, 235f, screenY - 325f, DispatcherOrientation.RIGHT)
    var dispatcherController: DispatcherController = DispatcherController(dispatcherLeft, dispatcherRight, 0.5f)

    var ground : DestroyingBorder = DestroyingBorder(world, Vector2(0f, 0f), Vector2(400f, 0f), Vector2(0f, 5f))
    var leftBorder : Border = Border(world, 3f, 800f, Vector2(1.5f, 400f))
    var rightBorder : Border = Border(world, 3f, 800f, Vector2(398.5f, 400f))
    init {
        world.setContactListener(contactListener)
        Gdx.input.inputProcessor = GestureDetector(dispatcherController)
        val greenHopper = Hopper(GameColor.GREEN, world, screenX * 0.5f, 0f)
        hopperList.add(greenHopper)
        val blueHopper = Hopper(GameColor.RED, world, screenX * 0.2f, 0f)
        hopperList.add(blueHopper)
        val redHopper = Hopper(GameColor.BLUE, world, screenX * 0.8f, 0f)
        hopperList.add(redHopper)
    }
}
