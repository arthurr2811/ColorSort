package com.colorsort.game.helpers

import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
import com.badlogic.gdx.utils.TimeUtils
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
        val maybeBallA = level.findBallFromFixture(fixtureA)
        val maybeBallB = level.findBallFromFixture(fixtureB)
        val maybeHopperA = level.findHopperFromFixture(fixtureA)
        val maybeHopperB = level.findHopperFromFixture(fixtureB)
        val maybeDestBorderA = level.findDestrBoarderFromFixture(fixtureA)
        val maybeDestBorderB = level.findDestrBoarderFromFixture(fixtureB)

        // case 1: if fixtureA belongs to a ball and fixtureB belongs to a hopper: same color -> score +1
        // different color -> game over
        if (maybeBallA != null && maybeHopperB != null){
            if (maybeBallA.getColor() == maybeHopperB.getColor()){
                level.increaseScore(1)
                level.playScoreSound()
            } else {
                level.playGameOverSound()
                level.gameOver()
            }
            level.deleteBall(maybeBallA)
        }
        // case 2: mirror of case 1
        else if ( maybeBallB != null && maybeHopperA != null){
            //same color score +1
            if (maybeBallB.getColor() == maybeHopperA.getColor()){
                level.increaseScore(1)
                level.playScoreSound()
            } else {
                level.playGameOverSound()
                level.gameOver()
            }
            level.deleteBall(maybeBallB)
        }
        // case 3: if fixtureA belongs to a ball and fixtureB belongs to a destr. border -> delete ball
        else if (maybeBallA != null &&  maybeDestBorderB != null){
            level.deleteBall(maybeBallA)
        }
        // case 4: mirror of case 3
        else if (maybeBallB != null && maybeDestBorderA != null){
            level.deleteBall(maybeBallB)
        }
        // case 5: balls collide -> just play collision sound
        else if (maybeBallA != null || maybeBallB != null){
            if (TimeUtils.nanoTime() - lastCollisionSoundPlayed > 500_000_000){
                level.playCollisionSound()
                lastCollisionSoundPlayed = TimeUtils.nanoTime()
            }

        }
    }
}
