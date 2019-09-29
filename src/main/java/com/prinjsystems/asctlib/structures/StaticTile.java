package com.prinjsystems.asctlib.structures;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A StaticTile is a tile that don't react to any events that may happen with it. It instead just "exists" there,
 * transferring heat from one place to another or being used with structural intent.
 */
public abstract class StaticTile extends Tile {
    private static final long serialVersionUID = -8394105447224475941L;

    protected StaticTile(int posX, int posY, Color color, String name) {
        super(posX, posY, color, name);
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillRect(getPosX() * TILE_SIZE, getPosY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public Color getColor() {
        return color;
    }
}
