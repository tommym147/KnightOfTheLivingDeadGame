package com.thomasmejia.knightofthelivingdead.Items;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by tommy on 4/25/2017.
 */

public class ItemDef {
    public Vector2 position;
    public Class<?> type;

    public ItemDef(Vector2 position, Class<?> type) {
        this.position = position;
        this.type = type;
    }
}
