package com.prinjsystems.asctlib.structures;

import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.List;

/**
 * Stores all the layers of an ASCT world. An ASCT save file ({@code .ssf} files) is composed of a serialized GameMap.
 */
public class GameMap implements Serializable {
    private static final long serialVersionUID = -5445523919009569824L;

    private List<Layer> layers; // A Deque would be great, but it is impossible to access n-th element in it
    private int currentLayer;

    /**
     * Creates a GameMap, already initialized with a list of layer in it.
     *
     * @param layers Layers to include in this GameMap.
     */
    public GameMap(List<Layer> layers) {
        this.layers = layers;
    }

    /**
     * Renders the current layer.
     *
     * @param g Graphics that will be used to render the current layer.
     */
    public void render(Graphics2D g) {
        layers.get(currentLayer).render(g);
    }

    /**
     * Tick all layers, in a first to last order.
     */
    public void tick() {
        for (Layer l : layers) {
            l.tick();
        }
    }

    public List<Layer> getLayers() {
        return layers;
    }

    public int getCurrentLayer() {
        return currentLayer;
    }

    public void setCurrentLayer(int currentLayer) {
        this.currentLayer = currentLayer;
    }

    /**
     * Move layer pointer by 1 unit up.
     */
    public void increaseLayer() {
        if (currentLayer < layers.size() - 1) {
            currentLayer++;
        } else {
            currentLayer = 0;
        }
    }

    /**
     * Move layer pointer by 1 unit down.
     */
    public void decreaseLayer() {
        if (currentLayer > 0) {
            currentLayer--;
        } else {
            currentLayer = layers.size() - 1;
        }
    }
}
