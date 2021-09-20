package org.narses.narsion.dev.world.narsionworlddata.npcs;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.narses.narsion.npc.Dialogue;
import org.narses.narsion.npc.NonPlayableCharacter;

import java.util.Arrays;
import java.util.UUID;

public enum NarsionNPCs {
    SHADY_MERCHANT(
            ShadyMerchant::new,
            Component.text("Shady Merchant"),
            new Pos(2934.54, 73.00, -84.48, -770.89F, 12.15F),
            Component.text("Hey kid, wanna buy a watch?"),
            Component.text("You're new around here. Watch your back or get attack.")
    ),
    FRIENDLY_MERCHANT(
            FriendlyMerchant::new,
            Component.text("Friendly Merchant"),
            new Pos(2933.54, 71.00, -62.51, -461.63F, 8.13F),
            Component.text("Doing well I see. Feel free to look around."),
            Component.text("Elsinore is where all the great conquerors began. It's the birthplace of kings.")
    ),
    VETERAN_MERCHANT(
            VeteranMerchant::new,
            Component.text("Veteran Merchant"),
            new Pos(2930.56, 69.00, -46.53, -496.53F, 1.03F),
            Component.text("Grgghmpg Kids these days.. hmghmr I remember steak pvp. The golden ages."),
            Component.text("What? A shield? Hit cooldown? You can't even jitter click can you chump.")
    ),
    ;

    final @NotNull NonPlayableCharacter.NPCSupplier supplier;
    final @NotNull UUID uuid;
    final @NotNull TextComponent displayName;
    final @NotNull Pos homePosition;
    final @NotNull TextComponent[] dialogue;

    NarsionNPCs(
            @NotNull NonPlayableCharacter.NPCSupplier supplier,
            @NotNull TextComponent displayName,
            @NotNull Pos homePosition,
            @NotNull TextComponent... dialogue
    ) {
        this.supplier = supplier;
        this.displayName = displayName;
        this.homePosition = homePosition;
        this.dialogue = Arrays.stream(dialogue)
                .map(component -> component.color(NamedTextColor.AQUA))
                .toArray(TextComponent[]::new);
        this.uuid = UUID.randomUUID();
    }

    public @NotNull NonPlayableCharacter spawn(@NotNull Instance instance) {

        // Create npc
        final NonPlayableCharacter npc = supplier.get(uuid, name(), homePosition, displayName);

        // Build dialogue
        buildDialogue(npc, instance);

        npc.setCustomName(displayName);

        npc.setInstance(instance, npc.homePosition());

        return npc;
    }

    private void buildDialogue(final @NotNull NonPlayableCharacter npc, @NotNull Instance instance) {
        if (dialogue.length == 0) {
            return;
        }

        final Hologram hologram = new Hologram(instance, npc.homePosition().add(0, npc.getEyeHeight() + 0.05, 0), Component.text(""));

        Dialogue.builder()
                .defaultRunner(hologram::setText)
                .add(dialogue)
                .build()
                .runAllForever();
    }
}
