package com.thomasmejia.knightofthelivingdead.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.thomasmejia.knightofthelivingdead.KnightOfTheLivingDead;
import com.thomasmejia.knightofthelivingdead.Screens.PlayScreen;
import com.thomasmejia.knightofthelivingdead.Sprites.Brick;
import com.thomasmejia.knightofthelivingdead.Sprites.Coin;
import com.thomasmejia.knightofthelivingdead.Enemies.Zombie;

/**
 * Created by tommy on 4/19/2017.
 */

public class B2WorldCreator {
    private Array<Zombie> zombies;

    public B2WorldCreator (PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        // create ground bodies/fixtures
        for (MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / KnightOfTheLivingDead.PPM, (rect.getY() + rect.getHeight()/2) / KnightOfTheLivingDead.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / KnightOfTheLivingDead.PPM, rect.getHeight()/2 / KnightOfTheLivingDead.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        // create pipe bodies/fixtures
        for (MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth()/2) / KnightOfTheLivingDead.PPM, (rect.getY() + rect.getHeight()/2) / KnightOfTheLivingDead.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth()/2 / KnightOfTheLivingDead.PPM, rect.getHeight()/2 / KnightOfTheLivingDead.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = KnightOfTheLivingDead.OBJECT_BIT;
            body.createFixture(fdef);
        }

        // create coin bodies/fixtures
        for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(screen,object);
        }

        // create brick bodies/fixtures
        for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen,object);
        }

        // create all zombies
        zombies = new Array<Zombie>();
        for (MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            zombies.add(new Zombie(screen,rect.getX()/KnightOfTheLivingDead.PPM, rect.getY()/KnightOfTheLivingDead.PPM));
        }
    }

    public Array<Zombie> getZombies() {
        return zombies;
    }
}
