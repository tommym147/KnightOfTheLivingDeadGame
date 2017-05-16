package com.thomasmejia.knightofthelivingdead.Enemies;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.thomasmejia.knightofthelivingdead.KnightOfTheLivingDead;
import com.thomasmejia.knightofthelivingdead.Screens.PlayScreen;

/**
 * Created by tommy on 4/24/2017.
 */

public class Zombie extends com.thomasmejia.knightofthelivingdead.Enemies.Enemy {

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Zombie(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 11; i <= 37; i+=26) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),i,7,16,32));
            walkAnimation = new Animation(0.4f, frames);
            stateTime = 0;
            setBounds(getX(),getY(),32/KnightOfTheLivingDead.PPM,32/KnightOfTheLivingDead.PPM);
            setToDestroy = false;
            destroyed = false;
        }
    }

    public void update(float dt) {
        stateTime += dt;
        if(setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),127,159,16,10));
            stateTime = 0;
        }
        else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
            setRegion((TextureRegion)walkAnimation.getKeyFrame(stateTime,true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(),getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / KnightOfTheLivingDead.PPM);
        fdef.filter.categoryBits = KnightOfTheLivingDead.ENEMY_BIT;
        fdef.filter.maskBits = KnightOfTheLivingDead.GROUND_BIT |
                KnightOfTheLivingDead.COIN_BIT |
                KnightOfTheLivingDead.BRICK_BIT |
                KnightOfTheLivingDead.ENEMY_BIT |
                KnightOfTheLivingDead.OBJECT_BIT |
                KnightOfTheLivingDead.BONEZ_BIT |
                KnightOfTheLivingDead.ENEMY_HEAD_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        // Create the Head here:
        PolygonShape head = new PolygonShape();
        Vector2 [] vertice = new Vector2[4];
        vertice[0] = new Vector2(-8,14).scl(1/KnightOfTheLivingDead.PPM);
        vertice[1] = new Vector2(8,14).scl(1/KnightOfTheLivingDead.PPM);
        vertice[2] = new Vector2(-6,6).scl(1/KnightOfTheLivingDead.PPM);
        vertice[3] = new Vector2(6,6).scl(1/KnightOfTheLivingDead.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = .5f;
        fdef.filter.categoryBits = KnightOfTheLivingDead.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        if(!destroyed || stateTime < 1) {
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
        KnightOfTheLivingDead.manager.get("audio/sounds/stomp.wav", Sound.class).play();

    }
}
