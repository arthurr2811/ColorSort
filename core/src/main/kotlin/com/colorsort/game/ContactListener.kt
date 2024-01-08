package com.colorsort.game

import com.badlogic.gdx.physics.box2d.CircleShape
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.physics.box2d.ContactImpulse
import com.badlogic.gdx.physics.box2d.ContactListener
import com.badlogic.gdx.physics.box2d.EdgeShape
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.Manifold
// whenever bodies collide contactListener functions are called
class ContactListener (private val ballsList : ArrayList<Ball>, private val ballsToRemoveList : ArrayList<Ball>) : ContactListener {
    override fun beginContact(contact: Contact?) {
        val fixtureA = contact?.fixtureA
        val fixtureB = contact?.fixtureB
        // if ball collides with hopper remove ball ToDO: if colors match update score otherwise game over
        if (isBallFixture(fixtureA) && isHopperFixture(fixtureB)){
            deleteBallFromFixture(fixtureA)
        } else if (isBallFixture(fixtureB) && isHopperFixture(fixtureA)) {
            deleteBallFromFixture(fixtureB)
        }
    }

    override fun endContact(contact: Contact?) {
    }

    override fun preSolve(contact: Contact?, oldManifold: Manifold?) {
    }

    override fun postSolve(contact: Contact?, impulse: ContactImpulse?) {
    }
    private fun isBallFixture(fixture: Fixture?) : Boolean{
        return if (fixture != null) {
            fixture.shape is CircleShape
        } else {
            false
        }
    }
    private fun isHopperFixture(fixture: Fixture?) : Boolean{
        return if (fixture != null) {
            // FixME: borders also edgeShape
            fixture.shape is EdgeShape
        } else {
            false
        }
    }
    private fun deleteBallFromFixture(ballFixture : Fixture?) {
        // remove ball from List and destroy its body (and with that also its fixture)
        if (ballFixture != null){
            val ballToRemove = ballsList.find { it.getBallFixture() == ballFixture }
            if (ballToRemove != null){
                // DON'T REMOVE DIRECT we are in a physics step: https://www.iforce2d.net/b2dtut/removing-bodies#:~:text=The%20actual%20code%20to%20remove,timestep%2C%20usually%20a%20collision%20callback.
                if (!ballsToRemoveList.contains(ballToRemove)){
                    ballsToRemoveList.add(ballToRemove)
                }
            }

        }
    }
}
