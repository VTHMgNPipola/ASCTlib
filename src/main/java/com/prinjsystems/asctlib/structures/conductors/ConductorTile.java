package com.prinjsystems.asctlib.structures.conductors;

import com.prinjsystems.asctlib.structures.ActionTile;
import com.prinjsystems.asctlib.structures.Tile;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This is the basic conductive tile. It will react to power trying to spread to it, and will spread power. Also can
 * be a via, that is a way to connect a tile from one layer to another layer.
 */
public abstract class ConductorTile extends ActionTile {
    private static final long serialVersionUID = -1614333273624363555L;

    private ActionTile connectedTo; // If connectedTo is not null then the conductor/wire is a via

    protected ConductorTile(int posX, int posY, Color color, String name) {
        super(posX, posY, color, name, false);
    }

    @Override
    public final void tick() {
        if (powered) {
            Tile[] tilesAround = from.getTilesAround(posX, posY);
            for (Tile tile : tilesAround) {
                if (!(tile instanceof ActionTile)) {
                    continue;
                }
                if (isValid(tile)) {
                    spread(tile);
                }
            }
            if (connectedTo != null) {
                spread(connectedTo);
            }
            postSpread();
        }
    }

    @Override
    public Color getColor() {
        return powered ? new Color(255, 191, 0) : color;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if (connectedTo != null) {
            g.setColor(new Color(255, 212, 0));
            g.drawRect(posX * TILE_SIZE, posY * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
        }
    }

    /**
     * Used to determine if the conductor should try to spread power to a tile in contact with it.
     *
     * @param tile Tile that should be checked.
     * @return If the tile passed as argument is valid or not.
     */
    protected boolean isValid(Tile tile) {
        return true;
    }

    /**
     * Will spread this tile's current powered state to another tile.
     *
     * @param tile Tile to try to spread to.
     */
    protected void spread(Tile tile) {
        ((ActionTile) tile).trySetPowered(powered, this);
    }

    /**
     * Actions done after spreading.
     */
    protected void postSpread() {
        powered = false;
    }

    public ActionTile getConnectedTo() {
        return connectedTo;
    }

    public void setConnectedTo(ActionTile connectedTo) {
        if (connectedTo != null && connectedTo.getPosX() == posX && connectedTo.getPosY() == posY
                && connectedTo != this) {
            this.connectedTo = connectedTo;
            if (connectedTo instanceof ConductorTile) {
                ((ConductorTile) connectedTo).setConnectedTo0(this);
            }
        } else if (connectedTo == null) {
            this.connectedTo = null;
        }
    }

    private void setConnectedTo0(ActionTile connectedTo) {
        this.connectedTo = connectedTo;
    }
}
