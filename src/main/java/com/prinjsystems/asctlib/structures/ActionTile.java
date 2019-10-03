package com.prinjsystems.asctlib.structures;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * An ActionTile is a tile that can react to events happening to it. For example a piece of wire. If another wire
 * tries to power it, it can get powered and try to transfer that power to other tiles around it (that can be other
 * wires or another conductive tile).
 */
public abstract class ActionTile extends Tile {
    private static final long serialVersionUID = -40261091317178060L;

    /**
     * How many updates the tile needs to "recover" from a powered state. Before that time runs out it cannot be
     * powered again.
     */
    protected int unpoweredDelay = 4;
    /**
     * Unpowered delay counter. When the tile is powered {@link #canReceivePower} is set to false this variable
     * starts incrementing with each tick. When this counter reaches {@link #unpoweredDelay} it goes back to 0,
     * {@link #canReceivePower} is set to true and the counter stops.
     */
    protected int unpoweredFor = 0;
    /**
     * Determines if the tile can be powered. This is set to false when it is powered, and back to true when the
     * unpowered delay runs out.
     */
    protected boolean canReceivePower = true;
    /**
     * Determines if the tile is currently powered.
     */
    protected boolean powered;

    protected ActionTile(int posX, int posY, Color color, String name, String shortenedName) {
        super(posX, posY, color, name, shortenedName);
    }

    public boolean isPowered() {
        return powered;
    }

    protected void setPowered(boolean powered) {
        this.powered = powered;
    }

    /**
     * Will try to set this tile to a powered or unpowered state. If {@link #canReceivePower} is true, then the tile
     * will be set its powered state to the 'powered' parameter.
     *
     * @param powered Power level to try set the tile into.
     * @param source  Source of the power. Some implementations may require a specific tile to be powered.
     */
    public void trySetPowered(boolean powered, Tile source) {
        if (canReceivePower) {
            this.powered = powered;
            canReceivePower = false;
            temp += 0.1f;
        }
    }

    @Override
    public void render(Graphics2D g) {
        // #getColor() is based on the implementation, it may change depending on the state of the tile
        g.setColor(getColor());
        g.fillRect(getPosX() * TILE_SIZE, getPosY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    /**
     * Will tick this tile, to, for example, try set tiles around it to a powered state if this tile is in a powered
     * state.
     */
    public abstract void tick();

    @Override
    public void update() {
        super.update();
        if (!canReceivePower) {
            if (++unpoweredFor == unpoweredDelay) {
                canReceivePower = true;
                unpoweredFor = 0;
            }
        }
    }

    public int getUnpoweredFor() {
        return unpoweredFor;
    }
}
