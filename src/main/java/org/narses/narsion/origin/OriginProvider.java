package org.narses.narsion.origin;

import org.jetbrains.annotations.NotNull;
import org.narses.narsion.NarsionServer;

import java.util.UUID;

// TODO: Async uploading origin to database

/**
 * Origins are records of tracked data. e.g. the origin of an item, or skillpoint.
 */
public class OriginProvider {

    private final @NotNull NarsionServer server;

    public OriginProvider(@NotNull NarsionServer server) {
        this.server = server;
    }

    /**
     * Generates an origin for a display location e.g. `npc:friendly_merchant`
     * @param displayLocation the location of where to display this item
     * @return the origin uuid
     */
    public @NotNull UUID DISPLAY(@NotNull String displayLocation) {
        return UUID.randomUUID();
    }

    public @NotNull UUID TRADE(
            @NotNull String tradeLocation,
            @NotNull String tradeInput,
            @NotNull String tradeOutput
    ) {
        return UUID.randomUUID();
    }
}
