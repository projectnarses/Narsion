package org.narses.narsion.dev;

import com.moandjiezana.toml.Toml;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.instance.*;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.classes.PlayerClasses;
import org.narses.narsion.dev.commands.ClassCommand;
import org.narses.narsion.dev.commands.GamemodeCommand;
import org.narses.narsion.dev.commands.ItemCommand;
import org.narses.narsion.dev.player.DevPlayer;
import org.narses.narsion.dev.events.DevEvents;
import org.narses.narsion.dev.items.DevelopmentItemData;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.dev.world.WorldDownloader;
import org.narses.narsion.dev.world.narsionworlddata.NarsionRegions;
import org.narses.narsion.region.RegionManager;
import org.narses.narsion.dev.world.blockhandlers.StaticBlocks;
import org.narses.narsion.dev.world.npc.NarsionNPCs;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * A development flavour of the NarsionServer used for testing purposes
 */
public class DevServer extends NarsionServer {
    // Entrypoint for dev server
    public static void main(final String[] args) {
        new DevServer(MinecraftServer.init());
    }

    private static final File PLAYER_CLASSES_CONFIG = new File("PlayerClasses.toml");

    private final RegionManager regionManager;
    private final InstanceContainer primaryInstance;

    /**
     * Initializes a dev server
     *
     * @param server the server being initialized
     */
    private DevServer(final MinecraftServer server) {
        super(
                server,
                MinecraftServer.getGlobalEventHandler(),
                new DevelopmentItemData(),
                DevPlayer::new,
                new PlayerClasses(new Toml().read(PLAYER_CLASSES_CONFIG)),
                List.of(NarsionNPCs.values())
        );

        // Try download world first
        try {
            new WorldDownloader(this).updateWorldFiles().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Start dev instance
        this.primaryInstance = MinecraftServer.getInstanceManager().createInstanceContainer();
        primaryInstance.setChunkGenerator(new DevelopmentChunkGenerator());
        // instanceContainer.setChunkLoader(new AnvilLoader("world"));

        // Setup regions
        this.regionManager = new RegionManager(this, NarsionRegions.values());

        // Spawn all npcs
        this.spawnNpcs(primaryInstance);

        // Register Events
        new DevEvents(this).registerAll(MinecraftServer.getGlobalEventHandler());

        // Register commands
        {
            final CommandManager manager = MinecraftServer.getCommandManager();

            manager.register(new ItemCommand(this));
            manager.register(new ClassCommand(this));
            manager.register(new GamemodeCommand(this));
        }

        // Register block handlers
        {
            final BlockManager manager = MinecraftServer.getBlockManager();

            for (final StaticBlocks staticBlock : StaticBlocks.values()) {
                manager.registerHandler(staticBlock.getNamespace(), staticBlock::create);
            }
        }

        server.start(config.getString("Server.IP"), config.getLong("Server.Port").intValue());
    }

    public @NotNull InstanceContainer getPrimaryInstance() {
        return primaryInstance;
    }

    private static class DevelopmentChunkGenerator implements ChunkGenerator {

        @Override
        public void generateChunkData(@NotNull final ChunkBatch batch, final int chunkX, final int chunkZ) {
            for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                    batch.setBlock(x, 0, z, Block.STONE);
                    batch.setBlock(x, 1, z, Block.STONE);
                    batch.setBlock(x, 2, z, Block.STONE);
                    batch.setBlock(x, 3, z, Block.STONE);
                    batch.setBlock(x, 4, z, Block.STONE);
                }
            }
        }

        @Override
        public void fillBiomes(@NotNull final Biome[] biomes, final int chunkX, final int chunkZ) {
            Arrays.fill(biomes, Biome.PLAINS);
        }

        @Override
        public @Nullable List<ChunkPopulator> getPopulators() {
            return null;
        }
    }
}
