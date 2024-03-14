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

// whenever bodies collide contactListener functions are called
class ContactListener (private val level: Level) : ContactListener {
    private var lastCollisionSoundPlayed = 0L
    override fun beginContact(contact: Contact?) {
        val fixtureA = contact?.fixtureA
        val fixtureB = contact?.fixtureB
        if (fixtureA != null && fixtureB != null){
            handleCollisionOutcome(fixtureA, fixtureB)
        }
    }

    override fun endContact(contact: Contact?) {
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }
    private fun findBallFromFixture(fixture: Fixture?) : Ball?{
        // if any ball.getfixure() in ballist == fixture the
        return level.ballsList.find { it.getFixture() == fixture }
    }
    private fun findHopperFromFixture(fixture: Fixture?) : Hopper?{
        return level.hopperList.find { it.getFixture() == fixture }
    }
    private fun findDestrBoarderFromFixture (fixture: Fixture?) : BallDestroyer? {
        return if(fixture == level.ground.getFixture()){
            level.ground
        } else {
            null
        }
    }
    private fun handleCollisionOutcome (fixtureA: Fixture, fixtureB: Fixture){
        // findClassFromFixtures returns the object of the Class or null, if fixture doesn't
        // belong to an object of the class. So if maybeBallA null we know fixtureA
        // didn't belong to a ball, otherwise we get the ball fixtureA belongs to.
        val maybeBallA = findBallFromFixture(fixtureA)
        val maybeBallB = findBallFromFixture(fixtureB)
        val maybeHopperA = findHopperFromFixture(fixtureA)
        val maybeHopperB = findHopperFromFixture(fixtureB)
        val maybeDestBorderA = findDestrBoarderFromFixture(fixtureA)
        val maybeDestBorderB = findDestrBoarderFromFixture(fixtureB)
        // if fixtureA belongs to a ball and fixtureB belongs to a hopper
        if (maybeBallA != null && maybeHopperB != null){
            //same color score +1
            if (maybeBallA.getColor() == maybeHopperB.getColor()){
                level.increaseScore(1)
                level.scoreSound.play(level.soundVolume)
            } else {
                level.gameOverSound.play(level.soundVolume)
                level.gameOver()
            }
            deleteBall(maybeBallA)
        }
        // if fixtureB belongs to a ball and fixtureA belongs to a hopper
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
        // if fixtureA belongs to a ball and fixtureB belongs to a destr border
        else if (maybeBallA != null &&  maybeDestBorderB != null){
            deleteBall(maybeBallA)
        }
        // if fixtureB belongs to a ball and fixtureA belongs to a destr border
        else if (maybeBallB != null && maybeDestBorderA != null){
            deleteBall(maybeBallB)
        }
        // if one or both is ball but no score and no game over play collision sound
        // don't play collision sound to frequently (max every 0.5 sec)
        else if (maybeBallA != null || maybeBallB != null){
            if (TimeUtils.nanoTime() - lastCollisionSoundPlayed > 500_000_000){
                level.ballCollissionSound.play(level.soundVolume)
                lastCollisionSoundPlayed = TimeUtils.nanoTime()
            }

        }
    }

    private fun deleteBall(ball : Ball?) {
        // add ball to remove list
        if (ball != null) {
            // DON'T REMOVE DIRECT we are in a physics step: https://www.iforce2d.net/b2dtut/removing-bodies#:~:text=The%20actual%20code%20to%20remove,timestep%2C%20usually%20a%20collision%20callback.
            if (!level.ballsToRemoveList.contains(ball)) {
                level.ballsToRemoveList.add(ball)
            }
        }
    }
}
