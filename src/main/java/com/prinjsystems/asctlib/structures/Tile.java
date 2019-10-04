package com.prinjsystems.asctlib.structures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Basic structure of the entire "game". A Tile is a "piece" that can be placed in the ASCT world, and may or may not
 * perform an action.
 * <p>
 * The Tile class itself is very primitive. It can only handle basic actions, such as heat spreading and gravity. To
 * create a Tile you will need to extend the class {@link StaticTile}, if you want a tile that don't actively do
 * something, or {@link ActionTile} if you want a tile that can respond to some actions (such as wires or logic gates).
 */
public abstract class Tile implements Serializable, Comparable {
    /**
     * This is the base size of a tile inside the game.
     */
    public static final int TILE_SIZE = 4;
    private static final long serialVersionUID = -2265316791600841307L;
    private static final Random random = new Random();
    /**
     * X position in cells of the tile inside its owning layer.
     */
    protected int posX;
    /**
     * Y position in cells of the tile inside its owning layer.
     */
    protected int posY;
    /**
     * Base tile color. Tiles generally have only one color, that is defined in this variable. If a tile has more
     * than one color (such as logic gates), than this variable can be used as background, for example, and a custom
     * rendering implementation will draw the rest of the tile.
     */
    protected Color color;
    /**
     * Indicates the "owning" layer of this tile. This is used to irradiate heat and to fall due to gravity.
     */
    protected Layer from;
    /**
     * Current temperature of the tile (in celsius).
     */
    protected float temp;
    /**
     * Melting temperature of the tile (in celsius).
     */
    protected float meltingTemp = 200;
    /**
     * How much heat the tile will irradiate to other tiles.
     */
    protected float irradiationRatio = 0.025f;
    /**
     * How much heat the tile will irradiate to the air.
     */
    protected float airIrradiationRatio = 0.015f;
    /**
     * Base viscosity. Determines how fast the tile will fall when molten, thought this is not a precise measurement,
     * since the viscosity is recalculated each time it falls by one unit to a new random value based on this base
     * viscosity. The lower the value the faster it will fall.
     */
    protected int viscosity = 2;
    /**
     * Full name of the tile.
     */
    protected String name;
    /**
     * Shortened name of the tile, shown in the tile selection menu in the game.
     */
    protected String shortenedName;
    private int currV = viscosity; // An int value that determines how fast it falls when molten
    private int vTick = 0; // Viscosity tick

    /**
     * Will create a tile with a X and Y position, a color and a name.
     */
    protected Tile(int posX, int posY, Color color, String name, String shortenedName) {
        this.posX = posX;
        this.posY = posY;
        this.color = color;
        this.name = name;
        this.shortenedName = shortenedName;
        temp = 27; // 27 Celsius
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortenedName() {
        return shortenedName;
    }

    public void setShortenedName(String shortenedName) {
        this.shortenedName = shortenedName;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    /**
     * This method is not intended to return the value of the {@link #color} property. It is instead meant to return
     * the color that should be used in the rendering process.
     * If you want to get the value of the color property, use {@link #getActualColor()} instead.
     *
     * @return The color that should be used during rendering.
     */
    public abstract Color getColor();

    /**
     * @return Value of the {@link #color} property.
     */
    public Color getActualColor() {
        return color;
    }

    public Layer getLayer() {
        return from;
    }

    public void setLayer(Layer from) {
        this.from = from;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    /**
     * Will render the tile. Generally the implementation will just render a filled square with the tile's color.
     *
     * @param g Graphics to draw the tile in.
     */
    public abstract void render(Graphics2D g);

    /**
     * Will update the tile's current status. In this base class a tick will care of transferring heat to surrounding
     * tiles or air, and process gravity if the tile is in a molten state.
     */
    public void update() {
        if (temp > meltingTemp) { // Is molten
            if (++vTick == currV) {
                if (from.getTile(posX, posY + 1) == null) { // Y + 1 is one below
                    from.swapTiles(posX, posY, posX, posY + 1);
                } else {
                    boolean leftFirst = random.nextBoolean();
                    if (leftFirst) {
                        if (from.getTile(posX - 1, posY + 1) == null) {
                            from.swapTiles(posX, posY, posX - 1, posY + 1);
                        } else if (from.getTile(posX + 1, posY + 1) == null) {
                            from.swapTiles(posX, posY, posX + 1, posY + 1);
                        }
                    } else {
                        if (from.getTile(posX + 1, posY + 1) == null) {
                            from.swapTiles(posX, posY, posX + 1, posY + 1);
                        } else if (from.getTile(posX - 1, posY + 1) == null) {
                            from.swapTiles(posX, posY, posX - 1, posY + 1);
                        }
                    }
                }
                vTick = 0;
                currV = ThreadLocalRandom.current().nextInt(Math.max(0, viscosity - 2), viscosity + 3);
            }
        }

        // Irradiate heat
        Tile[] tilesAround = from.getTilesAround(posX, posY);
        for (Tile t : tilesAround) {
            // Since the resistances would be so low, I decided to arbitrarily choose a "irradiation ratio" for
            // each material
            if (t == null && temp > 27) { // Let's make so that air cannot heat up and is at 27C
                temp -= temp * airIrradiationRatio;
            } else if (t != null && t.getTemp() < temp && temp > 27) {
                t.setTemp(t.getTemp() + temp * irradiationRatio);
                temp -= temp * irradiationRatio;
            }
        }
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Tile)) {
            throw new IllegalArgumentException("Illegal type '" + o.getClass().getName() + "' tried to be compared with '"
                    + Tile.class.getName() + "'!");
        }

        return name.compareTo(((Tile) o).getName());
    }
}
