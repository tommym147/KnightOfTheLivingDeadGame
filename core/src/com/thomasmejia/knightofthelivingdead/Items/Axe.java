package com.thomasmejia.knightofthelivingdead.Items;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.thomasmejia.knightofthelivingdead.KnightOfTheLivingDead;
import com.thomasmejia.knightofthelivingdead.Screens.PlayScreen;
import com.thomasmejia.knightofthelivingdead.Sprites.Bonez;

/**
 * Created by tommy on 4/20/2017.
 */

public class Axe extends Item {

    public Axe(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        setRegion(screen.getAtlas().findRegion("NES - Castlevania - Items"), 223,88,18,16);
        velocity = new Vector2(0,0);
    }

    @Override
    public void defineItem() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / KnightOfTheLivingDead.PPM);
        fdef.filter.categoryBits = KnightOfTheLivingDead.ITEM_BIT;
        fdef.filter.maskBits = KnightOfTheLivingDead.GROUND_BIT |
                KnightOfTheLivingDead.COIN_BIT |
                KnightOfTheLivingDead.BRICK_BIT |
                KnightOfTheLivingDead.OBJECT_BIT |
                KnightOfTheLivingDead.BONEZ_BIT;

        fdef.shape = shape;
        body.createFixture(fdef).setUserData(this);
    }

    @Override
    public void use(Bonez bonez) {
        bonez.grow();
        destroy();
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
        body.setLinearVelocity(velocity);
    }
}
