package com.prinjsystems.asctlib;

import com.prinjsystems.asctlib.structures.Tile;
import java.util.ArrayList;
import java.util.List;

/**
 * A TileCategory is, as the name implies, a category of tiles. A category of tiles is a "collection" of tiles, that
 * will be shown as a exclusive button in the tile selection panels of ASCT.
 */
public class TileCategory {
    private String name;
    private List<Tile> tiles;
    private int currentTile;

    TileCategory(String name) {
        this.name = name;
        tiles = new ArrayList<>();
        currentTile = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public void setTiles(List<Tile> tiles) {
        this.tiles = tiles;
    }

    /**
     * Will return the index of the currently selected Tile, instead of the Tile itself, as in {@link #getCurrentTile()}.
     *
     * @return Index of the currently selected Tile.
     */
    public int getCurrentTileIndex() {
        return currentTile;
    }

    public void setCurrentTileIndex(int currentTile) {
        this.currentTile = currentTile;
    }

    /**
     * Will return the Tile that is currently selected.
     *
     * @return Selected Tile.
     */
    public Tile getCurrentTile() {
        return tiles.get(currentTile);
    }
}
