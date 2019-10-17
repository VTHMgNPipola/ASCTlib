package com.prinjsystems.asctlib.structures;

import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import com.prinjsystems.asctlib.structures.conductors.light.Pixel;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Layer implements Serializable {
    /**
     * Size of each side of the layer. Each layer stores an array containing all of its tiles, and this array have
     * size LAYER_SIZE^2. The maximum value is 46340, that results in an array of almost maximum size. An array with
     * that many slots, however (around 2 billion and 150 million), would consume way too much memory, and I doubt it
     * would be any more useful than a layer of a third that size.
     */
    public static final int LAYER_SIZE = 1024; // Technically, the largest possible number is 46340, since the
    private static final long serialVersionUID = 2019849710583642798L;
    // largest int number, and so the largest array index, is 2 ^ 31 - 1, 65536 * 65536 is just big enough to not fit.
    // However, there are limitations to how many memory the JVM can allocate, and since the computer I'm using to
    // develop this "game" is basically a potato, there's not much to use.
    private Tile[] tiles; // Having a List would make it "infinite", but it will have an end anyway, and it is so
    // much faster to find tiles over an array than a List.

    /**
     * Creates a layer with tiles already in it.
     *
     * @param tiles Tiles to be added to that layer.
     */
    public Layer(List<Tile> tiles) {
        for (Tile t : tiles) {
            t.setLayer(this);
        }
        this.tiles = new Tile[LAYER_SIZE * LAYER_SIZE];
        for (Tile t : tiles) {
            this.tiles[t.getPosX() + t.getPosY() * LAYER_SIZE] = t;
            t.from = this;
        }
    }

    /**
     * Creates an empty layer.
     */
    public Layer() {
        this.tiles = new Tile[LAYER_SIZE * LAYER_SIZE];
    }

    void render(Graphics2D g) {
        for (Tile t : tiles) {
            if (t != null) {
                t.render(g);
            }
        }
    }

    /**
     * Updates all tiles inside this layer.
     */
    void tick() {
        // FIXME: Pixels that were just turned off need to tick
        // TODO: Optimize
        List<Tile> poweredTiles = new ArrayList<>();
        List<Tile> notNullTiles = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile != null) {
                if (tile instanceof ActionTile) {
                    if (((ActionTile) tile).isPowered() ||
                            (tile instanceof Pixel && ((Pixel) tile).isSpreading())) {
                        poweredTiles.add(tile);
                    }
                }
                notNullTiles.add(tile);
            }
        }
        poweredTiles.forEach(t -> ((ActionTile) t).tick()); // This is needed because otherwise tiles that are being
        // powered vertically, from top to bottom, would all work in the same tick.
        notNullTiles.forEach(Tile::update);
    }

    /**
     * Add (or replaces an existing) tile to this layer, and changes its ownership to this layer.
     *
     * @param tile Tile to be added to this layer.
     */
    public void addTile(Tile tile) {
        if (tile == null) {
            return;
        }
        tiles[tile.getPosX() + tile.getPosY() * LAYER_SIZE] = tile;
        tile.from = this;
    }

    /**
     * Removes (sets to null) a tile in the specified position.
     *
     * @param posX X position of the tile.
     * @param posY Y position of the tile.
     */
    public void removeTile(int posX, int posY) {
        Tile t = getTile(posX, posY);
        if (t instanceof ConductorTile && ((ConductorTile) t).getConnectedTo() != null) {
            if (((ConductorTile) t).getConnectedTo() instanceof ConductorTile) {
                ((ConductorTile) ((ConductorTile) t).getConnectedTo()).setConnectedTo(null);
            }
            ((ConductorTile) t).setConnectedTo(null);
        }
        tiles[posX + posY * LAYER_SIZE] = null;
    }

    /**
     * Will return the four tiles directly in contact with the tile in the specified position, that is, the tile at
     * the left, right, top and bottom of the specified position.
     *
     * @param x X position of the center tile.
     * @param y Y position of the center tile.
     * @return Tiles directly in contact with the center tile.
     */
    public Tile[] getTilesAround(int x, int y) {
        Tile[] result = new Tile[4];

        // All this looks horrible but removing a lot of logic and two for loops increases performance
        Tile t;
        if (x > 0 && (t = getTile(x - 1, y)) != null) {
            result[0] = t;
        }
        if (x < LAYER_SIZE - 1 && (t = getTile(x + 1, y)) != null) {
            result[1] = t;
        }
        if (y > 0 && (t = getTile(x, y - 1)) != null) {
            result[2] = t;
        }
        if (y < LAYER_SIZE - 1 && (t = getTile(x, y + 1)) != null) {
            result[3] = t;
        }

        return result;
    }

    public Tile getTile(int x, int y) {
        return tiles[x + y * LAYER_SIZE];
    }

    /**
     * Will change tile 1 (indicated by x1 and y1) to tile 2 (indicated by x2 and y2) and tile 2 to what tile 1 was.
     *
     * @param x1 X position of the first tile.
     * @param y1 Y position of the first tile.
     * @param x2 X position of the second tile.
     * @param y2 Y position of the second tile.
     */
    void swapTiles(int x1, int y1, int x2, int y2) {
        Tile t1 = getTile(x1, y1);
        if (t1 != null) {
            t1.setPosX(x2);
            t1.setPosY(y2);
        }
        Tile t2 = getTile(x2, y2);
        if (t2 != null) {
            t2.setPosX(x1);
            t2.setPosY(y1);
        }
        tiles[x2 + y2 * LAYER_SIZE] = t1;
        tiles[x1 + y1 * LAYER_SIZE] = t2;
    }
}
