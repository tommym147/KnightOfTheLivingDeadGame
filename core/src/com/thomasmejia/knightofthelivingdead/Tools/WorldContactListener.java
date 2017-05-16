package com.thomasmejia.knightofthelivingdead.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.thomasmejia.knightofthelivingdead.Items.Item;
import com.thomasmejia.knightofthelivingdead.KnightOfTheLivingDead;
import com.thomasmejia.knightofthelivingdead.Enemies.Enemy;
import com.thomasmejia.knightofthelivingdead.Sprites.Bonez;
import com.thomasmejia.knightofthelivingdead.Sprites.InteractiveTileObject;

/**
 * Created by tommy on 4/22/2017.
 */

public class WorldContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef) {
            case KnightOfTheLivingDead.BONEZ_HEAD_BIT | KnightOfTheLivingDead.BRICK_BIT:
            case KnightOfTheLivingDead.BONEZ_HEAD_BIT | KnightOfTheLivingDead.COIN_BIT:
                if (fixA.getFilterData().categoryBits == KnightOfTheLivingDead.BONEZ_HEAD_BIT)
                    ((InteractiveTileObject)fixB.getUserData()).onHeadHit((Bonez) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Bonez) fixB.getUserData());
                break;
            case KnightOfTheLivingDead.ENEMY_HEAD_BIT | KnightOfTheLivingDead.BONEZ_BIT:
                if (fixA.getFilterData().categoryBits == KnightOfTheLivingDead.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead();
                else
                    ((Enemy)fixB.getUserData()).hitOnHead();
                break;
            case KnightOfTheLivingDead.ENEMY_BIT | KnightOfTheLivingDead.OBJECT_BIT:
                if (fixA.getFilterData().categoryBits == KnightOfTheLivingDead.ENEMY_BIT)
                    ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                else
                    ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case KnightOfTheLivingDead.BONEZ_BIT | KnightOfTheLivingDead.ENEMY_BIT:
                if (fixA.getFilterData().categoryBits == KnightOfTheLivingDead.BONEZ_BIT)
                    ((Bonez) fixA.getUserData()).hit();
                else
                    ((Bonez) fixB.getUserData()).hit();
                break;
            case KnightOfTheLivingDead.ENEMY_BIT | KnightOfTheLivingDead.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).reverseVelocity(true,false);
                ((Enemy)fixB.getUserData()).reverseVelocity(true,false);
                break;
            case KnightOfTheLivingDead.ITEM_BIT | KnightOfTheLivingDead.BONEZ_BIT:
                if (fixA.getFilterData().categoryBits == KnightOfTheLivingDead.ITEM_BIT)
                    ((Item)fixA.getUserData()).use((Bonez) fixB.getUserData());
                else
                    ((Item)fixB.getUserData()).use((Bonez) fixA.getUserData());
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
