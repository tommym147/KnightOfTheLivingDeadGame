package com.thomasmejia.knightofthelivingdead.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.thomasmejia.knightofthelivingdead.KnightOfTheLivingDead;
import com.thomasmejia.knightofthelivingdead.Screens.PlayScreen;

/**
 * Created by tommy on 4/11/2017.
 */

public class Bonez extends Sprite {
    public enum State {FALLING, JUMPING, STANDING, RUNNING, GROWING, DEAD};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion bonezStand;
    private Animation bonezRun;
    private TextureRegion bonezJump;
    private TextureRegion bonezDead;
    private Animation bigBonezRun;
    private TextureRegion bigBonezStand;
    private TextureRegion bigBonezJump;
    private Animation growBonez;

    private float stateTimer;
    private boolean runningRight;
    private boolean bonezIsBig;
    private boolean runGrow;
    private boolean timeToDefineBonez;
    private boolean bonezIsDead;

    public Bonez (PlayScreen screen) {
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();

        // lil bonez
        frames.add(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),49,141,18,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),76,141,18,32));
        bonezRun = new Animation(0.1f, frames);
        frames.clear();

        // knight bonez
        frames.add(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),90,99,24,33));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),125,99,24,33));
        bigBonezRun = new Animation(0.1f, frames);
        frames.clear();

        // transforming animation
        frames.add(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),90,99,24,33));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),49,141,18,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),90,99,24,33));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),49,141,18,32));
        growBonez = new Animation(0.2f, frames);

        bonezJump = new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),76,141,18,32);
        bigBonezJump = new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),125,99,24,33);

        bonezStand = new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),49,141,18,32);
        bigBonezStand = new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),90,99,24,33);

        bonezDead = new TextureRegion(screen.getAtlas().findRegion("NES - Castlevania - Enemies"),100,151,16,18);
        defineBonez();

        setBounds(49,141,32/KnightOfTheLivingDead.PPM,32 / KnightOfTheLivingDead.PPM);
        setRegion(bonezStand);
    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));

        if(timeToDefineBonez)
            redefineBonez();
    }

    public boolean isBig() {
        return bonezIsBig;
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region = bonezDead;
                break;
            case GROWING:
                region = (TextureRegion) growBonez.getKeyFrame(stateTimer);
                if (growBonez.isAnimationFinished(stateTimer))
                    runGrow = false;
                break;
            case JUMPING:
                region = bonezIsBig ? bigBonezJump : bonezJump;
                break;
            case RUNNING:
                region = bonezIsBig ? (TextureRegion) bigBonezRun.getKeyFrame(stateTimer,true) : (TextureRegion) bonezRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = bonezIsBig ? bigBonezStand:bonezStand;
                break;
        }
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && region.isFlipX()) {
            region.flip(true,false);
            runningRight = false;
        }
        else if ((b2body.getLinearVelocity().x > 0 || runningRight) && !region.isFlipX()) {
            region.flip(true,false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (bonezIsDead)
            return State.DEAD;
        else if (runGrow)
            return State.GROWING;
        else if(b2body.getLinearVelocity().y > 0 || b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING)
            return State.JUMPING;
        else if (b2body.getLinearVelocity().y < 0)
            return State.FALLING;
        else if (b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;
    }

    public void grow() {
        runGrow = true;
        bonezIsBig = true;
        setBounds(getX(),getY(),getWidth(),getHeight());
        KnightOfTheLivingDead.manager.get("audio/sounds/Transforming.wav", Sound.class).play();
    }

    public void redefineBonez() {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / KnightOfTheLivingDead.PPM);
        fdef.filter.categoryBits = KnightOfTheLivingDead.BONEZ_BIT;
        fdef.filter.maskBits = KnightOfTheLivingDead.GROUND_BIT |
                KnightOfTheLivingDead.COIN_BIT |
                KnightOfTheLivingDead.BRICK_BIT |
                KnightOfTheLivingDead.ENEMY_BIT |
                KnightOfTheLivingDead.OBJECT_BIT |
                KnightOfTheLivingDead.ENEMY_HEAD_BIT |
                KnightOfTheLivingDead.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / KnightOfTheLivingDead.PPM, 12 / KnightOfTheLivingDead.PPM), new Vector2(2 / KnightOfTheLivingDead.PPM, 12 / KnightOfTheLivingDead.PPM));
        fdef.filter.categoryBits = KnightOfTheLivingDead.BONEZ_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);

        timeToDefineBonez = false;
    }

    public void hit() {
        if(bonezIsBig) {
            bonezIsBig = false;
            setBounds(getX(),getY(),getWidth(),getHeight());
            KnightOfTheLivingDead.manager.get("audio/sounds/powerdown.wav", Sound.class).play();
        }
        else {
            KnightOfTheLivingDead.manager.get("audio/music/Dio - Stand Up And Shout (8 bit).ogg", Music.class).stop();
            KnightOfTheLivingDead.manager.get("audio/sounds/Death.wav", Sound.class).play();
            bonezIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = KnightOfTheLivingDead.NOTHING_BIT;
            for(Fixture fixture: b2body.getFixtureList())
                fixture.setFilterData(filter);
            b2body.applyLinearImpulse(new Vector2(0,4f),b2body.getWorldCenter(),true);
        }
    }

    public boolean isDead() {
        return bonezIsDead;
    }

    public float getStateTimer() {
        return stateTimer;
    }

    public void defineBonez() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(32/ KnightOfTheLivingDead.PPM,32/ KnightOfTheLivingDead.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(12 / KnightOfTheLivingDead.PPM);
        fdef.filter.categoryBits = KnightOfTheLivingDead.BONEZ_BIT;
        fdef.filter.maskBits = KnightOfTheLivingDead.GROUND_BIT |
                KnightOfTheLivingDead.COIN_BIT |
                KnightOfTheLivingDead.BRICK_BIT |
                KnightOfTheLivingDead.ENEMY_BIT |
                KnightOfTheLivingDead.OBJECT_BIT |
                KnightOfTheLivingDead.ENEMY_HEAD_BIT |
                KnightOfTheLivingDead.ITEM_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / KnightOfTheLivingDead.PPM, 12 / KnightOfTheLivingDead.PPM), new Vector2(2 / KnightOfTheLivingDead.PPM, 12 / KnightOfTheLivingDead.PPM));
        fdef.filter.categoryBits = KnightOfTheLivingDead.BONEZ_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }
}
