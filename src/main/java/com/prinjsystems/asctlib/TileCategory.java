package com.prinjsystems.asctlib;

import com.prinjsystems.asctlib.structures.Tile;
import java.util.ArrayList;
import java.util.List;

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

    public int getCurrentTileIndex() {
        return currentTile;
    }

    public void setCurrentTileIndex(int currentTile) {
        this.currentTile = currentTile;
    }

    public Tile getCurrentTile() {
        return tiles.get(currentTile);
    }
}
