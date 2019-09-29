package com.prinjsystems.asctlib;

/**
 * The "main" class of an ASCT mod should extend this class to be recognized by ASCT.
 * The {@link #startup()} method will be executed before the game initialization procedure, that will search for all
 * the tiles containing the {@link PlaceableTile} annotation.
 */
public abstract class ASCTMod {
    /**
     * Method called by ASCT before the game initialization procedure.
     * Should be used to register necessary tile categories, if they exist.
     */
    public abstract void startup();
}
