package org.narses.narsion;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.ChunkGenerator;
import net.minestom.server.instance.ChunkPopulator;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.batch.ChunkBatch;
import net.minestom.server.instance.block.Block;
import net.minestom.server.world.biomes.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class DevServer extends NarsionServer {
    // Entrypoint for dev server
    public static void main(String[] args) {
        new DevServer(MinecraftServer.init());
    }

    private static final Pos SPAWN_POSITION = new Pos(0, 10, 0);

    /**
     * Initializes a dev server
     *
     * @param server
     */
    private DevServer(MinecraftServer server) {
        super(server);

        // Start dev instance
        InstanceContainer instanceContainer = MinecraftServer.getInstanceManager().createInstanceContainer();
        instanceContainer.setChunkGenerator(new DevelopmentChunkGenerator());

        // Proper spawning
        MinecraftServer.getGlobalEventHandler()
                .addListener(PlayerLoginEvent.class, event -> event.setSpawningInstance(instanceContainer))
                .addListener(PlayerSpawnEvent.class, event -> event.getPlayer().teleport(SPAWN_POSITION));

        server.start("0.0.0.0", 25565);
    }

    private class DevelopmentChunkGenerator implements ChunkGenerator {

        @Override
        public void generateChunkData(@NotNull ChunkBatch batch, int chunkX, int chunkZ) {
            for (int x = 0; x < Chunk.CHUNK_SIZE_X; x++) {
                for (int z = 0; z < Chunk.CHUNK_SIZE_Z; z++) {
                    batch.setBlock(x, 0, z, Block.STONE);
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
