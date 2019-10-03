package com.prinjsystems.asctlib.structures.conductors.semiconductors;

import com.prinjsystems.asctlib.structures.conductors.ConductorTile;
import java.awt.Color;

public class PSilicon extends ConductorTile {
    private static final long serialVersionUID = 7373229821931252770L;

    public PSilicon(int posX, int posY) {
        super(posX, posY, new Color(156, 10, 10), "P-type Silicon", "PSLC");
    }
}
