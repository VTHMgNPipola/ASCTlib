package com.prinjsystems.asctlib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton class that holds the categories of ASCT tiles.
 * Categories in ASCT are used to separate one type of tile from another, into different tabs inside the game. Each
 * category is a different tab.
 */
public class TileCategoryHolder {
    private static final TileCategoryHolder instance = new TileCategoryHolder();

    private List<TileCategory> categories;

    private TileCategoryHolder() {
        categories = new ArrayList<>();
        registerCategory("structural");
        registerCategory("logic");
    }

    public static TileCategoryHolder getInstance() {
        return instance;
    }

    /**
     * Will register a new tile category.
     * This method should be called before the game initialization procedure, since after that the game will not
     * search for tiles or categories again.
     * If a tile is annotated with {@link PlaceableTile} and the category defined in it does not exist, an exception
     * will be thrown.
     *
     * @param category Category to be registered.
     */
    public void registerCategory(String category) {
        for (TileCategory ctg : categories) {
            if (ctg.getName().equals(category.toLowerCase())) {
                throw new IllegalArgumentException("Category '" + category + "' already exists!");
            }
        }
        categories.add(new TileCategory(category.toLowerCase()));
    }

    /**
     * @return Unmodifiable version of the list of tile categories.
     */
    public List<TileCategory> getCategories() {
        return Collections.unmodifiableList(categories);
    }
}
