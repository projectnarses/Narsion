package org.narses.narsion.dev.world.blockhandlers;

import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.tag.Tag;
import net.minestom.server.utils.NamespaceID;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class StaticBlock implements BlockHandler {


    private final @NotNull NamespaceID namespaceID;
    private final @NotNull Collection<Tag<?>> publicTags;

    /**
     * Generates a static block
     * @param namespace the namespace to register the block to
     * @param publicTags the tags to expose to the client
     */
    public StaticBlock(@NotNull String namespace, @NotNull Collection<Tag<?>> publicTags) {
        this.namespaceID = NamespaceID.from(namespace);
        this.publicTags = publicTags;
    }

    @Override
    public @NotNull NamespaceID getNamespaceId() {
        return namespaceID;
    }

    @Override
    public @NotNull Collection<Tag<?>> getBlockEntityTags() {
        return publicTags;
    }
}
