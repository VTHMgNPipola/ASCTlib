package com.prinjsystems.asctlib.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.structures.Tile;
import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import java.awt.Color;

public class NSilicon extends ConductorTile {
    private static final long serialVersionUID = -649556609668628613L;

    public NSilicon(int posX, int posY) {
        super(posX, posY, new Color(50, 100, 230), "N-type Silicon");
    }

    @Override
    protected boolean isValid(Tile tile) {
        return !(tile instanceof PSilicon);
    }
}
