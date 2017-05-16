package com.thomasmejia.knightofthelivingdead.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.thomasmejia.knightofthelivingdead.Items.Axe;
import com.thomasmejia.knightofthelivingdead.Items.Item;
import com.thomasmejia.knightofthelivingdead.Items.ItemDef;
import com.thomasmejia.knightofthelivingdead.KnightOfTheLivingDead;
import com.thomasmejia.knightofthelivingdead.Scenes.Hud;
import com.thomasmejia.knightofthelivingdead.Sprites.Bonez;
import com.thomasmejia.knightofthelivingdead.Enemies.Enemy;
import com.thomasmejia.knightofthelivingdead.Tools.B2WorldCreator;
import com.thomasmejia.knightofthelivingdead.Tools.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by tommy on 2/28/2017.
 */

public class PlayScreen implements Screen {

    private KnightOfTheLivingDead game;
    private TextureAtlas atlas;

    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    // Tiled map variables
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // Box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    // sprites
    private Bonez player;

    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDef> itemsToSpawn;


    public PlayScreen(KnightOfTheLivingDead game) {
        atlas = new TextureAtlas("Bonez_and_enemies.pack");

        this.game = game;
        // create cam used to follow Bonez through cam world
        gamecam = new OrthographicCamera();

        // create a FitViewport to maintain virtual aspect ratio
        gamePort = new FitViewport(KnightOfTheLivingDead.V_WIDTH / KnightOfTheLivingDead.PPM,KnightOfTheLivingDead.V_HEIGHT / KnightOfTheLivingDead.PPM,gamecam);

        // create our game HUD for scores/timers/level info
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("knightmap1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / KnightOfTheLivingDead.PPM);

        // initially set our gamecam to be centered correctly
        gamecam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);


        world = new World(new Vector2(0,-10), true);
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        // create main character Bonez
        player = new Bonez(this);

        world.setContactListener(new WorldContactListener());

        music = KnightOfTheLivingDead.manager.get("audio/music/Dio - Stand Up And Shout (8 bit).ogg",Music.class);
        music.setLooping(true);
        music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDef>();
    }

    public void spawnItem(ItemDef idef) {
        itemsToSpawn.add(idef);
    }

    public void handleSpawningItems() {
        if(!itemsToSpawn.isEmpty()) {
            ItemDef idef = itemsToSpawn.poll();
            if(idef.type == Axe.class) {
                items.add(new Axe(this,idef.position.x,idef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        // If our user is holding down mouse move our camera through the game world
        if(player.currentState != Bonez.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt) {
        handleInput(dt);
        handleSpawningItems();

        world.step(1/60f,6,2);

        player.update(dt);
        for(Enemy enemy: creator.getZombies()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224/KnightOfTheLivingDead.PPM) {
                enemy.b2body.setActive(true);
            }
        }

        for(Item item: items) {
            item.update(dt);
        }

        hud.update(dt);

        if (player.currentState != Bonez.State.DEAD) {
            gamecam.position.x = player.b2body.getPosition().x;
        }

        gamecam.update();
        renderer.setView(gamecam);
    }

    public boolean gameOver() {
        if(player.currentState == Bonez.State.DEAD && player.getStateTimer() > 3) {
            return true;
        }
        return false;
    }

    @Override
    public void render(float delta) {
        // separate our update logic from render
        update(delta);

        // Clear the game screen with Black
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render our game map
        renderer.render();

        // renderer our Box2DDebugLines
        b2dr.render(world,gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy: creator.getZombies())
            enemy.draw(game.batch);
        for (Item item: items) {
            item.draw(game.batch);
        }
        game.batch.end();

        // set our batch to now draw what the Hud camera sees.
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if(gameOver()) {
            game.setScreen(new GameOverScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);
    }

    public TiledMap getMap () {
        return map;
    }

    public World getWorld () {
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();

    }
}
