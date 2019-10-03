package com.prinjsystems.asctlib.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.structures.Tile;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A Logic Gate is a form of transistor that can accept or deny power to pass throughout it, depending on the inputs.
 */
public abstract class LogicGate extends Transistor {
    private static final long serialVersionUID = -6760888350939398358L;

    private Color insideColor;

    protected LogicGate(int posX, int posY, Color color, String name, String shortenedName) {
        super(posX, posY);
        this.shortenedName = shortenedName;
        this.name = name;
        this.insideColor = color;
        conductive = true;
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        g.setColor(insideColor);
        g.fillRect(posX * TILE_SIZE + 1, posY * TILE_SIZE + 1, TILE_SIZE - 2, TILE_SIZE - 2);
    }

    @Override
    public void update() {
        super.update();
        conductiveFor = 0;
    }

    @Override
    protected boolean isValid(Tile tile) {
        return !(tile instanceof NSilicon);
    }
}
