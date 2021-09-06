package org.narses.narsion.dev;

import com.moandjiezana.toml.Toml;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandManager;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.*;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.BlockHandler;
import net.minestom.server.instance.block.BlockManager;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.narses.narsion.classes.PlayerClasses;
import org.narses.narsion.dev.commands.ClassCommand;
import org.narses.narsion.dev.commands.GamemodeCommand;
import org.narses.narsion.dev.commands.ItemCommand;
import org.narses.narsion.dev.entity.player.DevPlayer;
import org.narses.narsion.dev.items.DevelopmentItemData;
import org.narses.narsion.NarsionServer;
import org.narses.narsion.dev.region.Region;
import org.narses.narsion.dev.region.RegionManager;
import org.narses.narsion.dev.region.Regions;
import org.narses.narsion.dev.region.regions.Elsinore;
import org.narses.narsion.dev.world.WorldDownloader;
import org.narses.narsion.dev.world.blockhandlers.StaticBlocks;
import org.narses.narsion.player.NarsionPlayer;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A development flavour of the NarsionServer used for testing purposes
 */
public class DevServer extends NarsionServer {
    // Entrypoint for dev server
    public static void main(String[] args) {
        new DevServer(MinecraftServer.init());
    }

    private static final File PLAYER_CLASSES_CONFIG = new File("PlayerClasses.toml");

    private final RegionManager regionManager = new RegionManager(this);

    /**
     * Initializes a dev server
     *
     * @param server the server being initialized
     */
    private DevServer(MinecraftServer server) {
        super(
                server,
                new DevelopmentItemData(),
                DevPlayer::new,
                new PlayerClasses(new Toml().read(PLAYER_CLASSES_CONFIG))
        );

        // Start dev instance
        InstanceContainer instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();
        instanceContainer.setChunkGenerator(new DevelopmentChunkGenerator());
        // instanceContainer.setChunkLoader(new AnvilLoader("world"));

        // Load regions
        Region[] regions = Arrays.stream(Regions.values())
                .map(Regions::getRegion)
                .toArray(Region[]::new);
        regionManager.addRegion(regions);


        // Get respawn point from config
        double worldSpawnX = config.getDouble("World.SpawnX");
        double worldSpawnY = config.getDouble("World.SpawnY");
        double worldSpawnZ = config.getDouble("World.SpawnZ");

        Pos respawnPoint = new Pos(worldSpawnX, worldSpawnY, worldSpawnZ);

        // Proper spawning
        MinecraftServer.getGlobalEventHandler()
                .addListener(PlayerLoginEvent.class, event -> event.setSpawningInstance(instanceContainer))
                .addListener(PlayerLoginEvent.class, event -> event.getPlayer().setRespawnPoint(respawnPoint))
                .addListener(PlayerLoginEvent.class, event -> event.getPlayer().setPermissionLevel(4))

                // Testing stuffs
                .addListener(PlayerSpawnEvent.class, event -> {

                    event.getPlayer().setItemInMainHand(
                            this.getItemStackProvider()
                                    .create(
                                            "THE_END",
                                            new UUID(0, 0),
                                            null
                                    )
                    );

                    for (Regions region : Regions.values()) {
                        region.getRegion().addViewer(event.getPlayer());
                    }

                });

        // Register commands
        {
            CommandManager manager = MinecraftServer.getCommandManager();

            manager.register(new ItemCommand(this));
            manager.register(new ClassCommand(this));
            manager.register(new GamemodeCommand(this));
        }

        // Register block handlers
        {
            BlockManager manager = MinecraftServer.getBlockManager();

            for (StaticBlocks staticBlock : StaticBlocks.values()) {
                manager.registerHandler(staticBlock.getNamespace(), staticBlock::create);
            }
        }

        try {
            WorldDownloader.ensureLatestWorld(config);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        server.start(config.getString("Server.IP"), config.getLong("Server.Port").intValue());
    }

    private static class DevelopmentChunkGenerator implements ChunkGenerator {

        @Override
        public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
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
        public void fillBiomes(@NotNull Biome[] biomes, int chunkX, int chunkZ) {
            Arrays.fill(biomes, Biome.PLAINS);
        }

        @Override
        public @Nullable List<ChunkPopulator> getPopulators() {
            return null;
        }
    }
}
