package com.thomasmejia.knightofthelivingdead.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.thomasmejia.knightofthelivingdead.KnightOfTheLivingDead;
import com.thomasmejia.knightofthelivingdead.Scenes.Hud;
import com.thomasmejia.knightofthelivingdead.Screens.PlayScreen;

/**
 * Created by tommy on 4/22/2017.
 */

public class Brick extends InteractiveTileObject {

    public Brick(PlayScreen screen, MapObject object) {
        super(screen, object);
        fixture.setUserData(this);
        setCategoryFilter(KnightOfTheLivingDead.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Bonez bonez) {
        if (bonez.isBig()) {
            setCategoryFilter(KnightOfTheLivingDead.DESTROYED_BIT);
            getCell().setTile(null);
            Hud.addScore(200);
            KnightOfTheLivingDead.manager.get("audio/sounds/breakblock.wav", Sound.class).play();
        }
        KnightOfTheLivingDead.manager.get("audio/sounds/bump.wav", Sound.class).play();
    }
}
