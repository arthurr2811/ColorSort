package com.colorsort.game.helpers

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.utils.TimeUtils
import com.colorsort.game.gameObjects.Ball
import com.colorsort.game.gameObjects.BallDestroyer
import com.colorsort.game.gameObjects.Hopper
import com.colorsort.game.levels.Level

/*
 whenever bodies in our box2d world collide contactListener functions are called
 */
class ContactListener (private val level: Level) : ContactListener {
    // many collisions may happen in a short period of time, but we dont want to play the sound
    // many times in a short period of time
    private var lastCollisionSoundPlayed = 0L
    override fun beginContact(contact: Contact?) {
        val fixtureA = contact?.fixtureA
        val fixtureB = contact?.fixtureB
        if (fixtureA != null && fixtureB != null){
            // collision handler only gives us the fixture of the two objects collided
            // we need to find out what objects collided and act correspondingly
            handleCollisionOutcome(fixtureA, fixtureB)
        }
    }

    override fun endContact(contact: Contact?) {
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }
    private fun handleCollisionOutcome (fixtureA: Fixture, fixtureB: Fixture){
        /*
        we need to find out what objects collided (only having the fixtures)and act correspondingly.
        So we go trough all our game objects and try to find the one, the fixture belongs to. Then we
        now what game objects collided so we can decide how to react
         */
        val maybeBallA = findBallFromFixture(fixtureA)
        val maybeBallB = findBallFromFixture(fixtureB)
        val maybeHopperA = findHopperFromFixture(fixtureA)
        val maybeHopperB = findHopperFromFixture(fixtureB)
        val maybeDestBorderA = findDestrBoarderFromFixture(fixtureA)
        val maybeDestBorderB = findDestrBoarderFromFixture(fixtureB)

        // case 1: if fixtureA belongs to a ball and fixtureB belongs to a hopper: same color -> score +1
        // different color -> game over
        if (maybeBallA != null && maybeHopperB != null){
            if (maybeBallA.getColor() == maybeHopperB.getColor()){
                level.increaseScore(1)
                level.scoreSound.play(level.soundVolume)
            } else {
                level.gameOverSound.play(level.soundVolume)
                level.gameOver()
            }
            deleteBall(maybeBallA)
        }
        // case 2: mirror of case 1
        else if ( maybeBallB != null && maybeHopperA != null){
            //same color score +1
            if (maybeBallB.getColor() == maybeHopperA.getColor()){
                level.increaseScore(1)
                level.scoreSound.play(level.soundVolume)
            } else {
                level.gameOverSound.play(level.soundVolume)
                level.gameOver()
            }
            deleteBall(maybeBallB)
        }
        // case 3: if fixtureA belongs to a ball and fixtureB belongs to a destr. border -> delete ball
        else if (maybeBallA != null &&  maybeDestBorderB != null){
            deleteBall(maybeBallA)
        }
        // case 4: mirror of case 3
        else if (maybeBallB != null && maybeDestBorderA != null){
            deleteBall(maybeBallB)
        }
        // case 5: balls collide -> just play collision sound
        else if (maybeBallA != null || maybeBallB != null){
            if (TimeUtils.nanoTime() - lastCollisionSoundPlayed > 500_000_000){
                level.ballCollisionSound.play(level.soundVolume)
                lastCollisionSoundPlayed = TimeUtils.nanoTime()
            }

        }
    }
    private fun findBallFromFixture(fixture: Fixture?) : Ball?{
        // if any ball.getfixure() in ballL ist -> fixture is from a ball
        return level.ballsList.find { it.getFixture() == fixture }
    }
    private fun findHopperFromFixture(fixture: Fixture?) : Hopper?{
        // if any hopper.getfixure() in hopperList -> fixture is from a hopper
        return level.hopperList.find { it.getFixture() == fixture }
    }
    private fun findDestrBoarderFromFixture (fixture: Fixture?) : BallDestroyer? {
        return if(fixture == level.ground.getFixture()){
            level.ground
        } else {
            null
        }
    }
    private fun deleteBall(ball : Ball?) {
        // DON'T REMOVE DIRECTLY we are in a physics step:
        // https://www.iforce2d.net/b2dtut/removing-bodies#:~:text=The%20actual%20code%20to%20remove,timestep%2C%20usually%20a%20collision%20callback.
        // add ball to remove list, remove later
        if (ball != null) {
            if (!level.ballsToRemoveList.contains(ball)) {
                level.ballsToRemoveList.add(ball)
            }
        }
    }
}
