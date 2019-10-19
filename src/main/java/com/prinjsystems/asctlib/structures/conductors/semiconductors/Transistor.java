package com.prinjsystems.asctlib.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.PlaceableTile;
import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import java.awt.Color;

@PlaceableTile("logic")
public class Transistor extends ConductorTile {
    private static final long serialVersionUID = 3056763776572443061L;

    protected boolean conductive;
    protected int conductiveDelay = 4;
    protected Color conductiveColor;
    protected int conductiveFor;

    public Transistor(int posX, int posY) {
        super(posX, posY, new Color(103, 75, 120), "Transistor", "TRST");
        conductiveColor = new Color(220, 159, 255);
        meltingTemp = 140f;
    }

    public boolean isConductive() {
        return conductive;
    }

    @Override
    public void trySetPowered(boolean powered, Tile source) {
        if (source != null && canReceivePower) {
            if (source instanceof NSilicon) {
                conductive = true;
                conductiveFor = 0;
            } else if (source instanceof PSilicon && conductive) {
                super.trySetPowered(true, null);
            }
        } else {
            super.trySetPowered(powered, null);
        }
    }

    @Override
    public void update() {
        super.update();
        if (conductive) {
            if (++conductiveFor == conductiveDelay) {
                canReceivePower = true;
                conductive = false;
            }
        }
    }

    @Override
    public Color getColor() {
        // super#getColor() will always return the "turned on" color, since it will only be called when "powered"
        return powered ? super.getColor() : conductive ? conductiveColor : color;
    }

    @Override
    protected boolean isValid(Tile tile) {
        return !(tile instanceof PSilicon) && !(tile instanceof NSilicon);
    }
}
