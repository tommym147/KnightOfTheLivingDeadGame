package com.thomasmejia.knightofthelivingdead.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.thomasmejia.knightofthelivingdead.Items.*;
import com.thomasmejia.knightofthelivingdead.KnightOfTheLivingDead;
import com.thomasmejia.knightofthelivingdead.Scenes.Hud;
import com.thomasmejia.knightofthelivingdead.Screens.PlayScreen;

/**
 * Created by tommy on 4/20/2017.
 */

public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN = 28;
    public Coin (PlayScreen screen, MapObject object) {
        super(screen, object);
        tileSet = map.getTileSets().getTileSet("tileset_gutter");
        fixture.setUserData(this);
        setCategoryFilter(KnightOfTheLivingDead.COIN_BIT);
    }

    @Override
    public void onHeadHit(Bonez bonez) {
        if (getCell().getTile().getId() == BLANK_COIN) {
            KnightOfTheLivingDead.manager.get("audio/sounds/bump.wav", Sound.class).play();
        }
        else {
            if (object.getProperties().containsKey("axe")) {
                screen.spawnItem(new ItemDef(new Vector2(body.getPosition().x, body.getPosition().y + 16 / KnightOfTheLivingDead.PPM),
                        com.thomasmejia.knightofthelivingdead.Items.Axe.class));
                KnightOfTheLivingDead.manager.get("audio/sounds/powerup_spawn.wav", Sound.class).play();
            }
            else
                KnightOfTheLivingDead.manager.get("audio/sounds/coin.wav", Sound.class).play();
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));
        Hud.addScore(300);
    }

}
