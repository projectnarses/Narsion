package org.itemize.data;

import net.kyori.adventure.text.Component;
import org.jglrxavpok.hephaistos.nbt.NBTCompound;

public record ItemRarity(Component displayName, NBTCompound data) {}
