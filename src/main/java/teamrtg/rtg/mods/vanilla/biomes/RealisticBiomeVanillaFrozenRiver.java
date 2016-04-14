package teamrtg.rtg.mods.vanilla.biomes;

import net.minecraft.init.Biomes;
import teamrtg.rtg.mods.vanilla.surfaces.SurfaceVanillaFrozenRiver;
import teamrtg.rtg.util.noise.CellNoise;
import teamrtg.rtg.util.noise.OpenSimplexNoise;
import teamrtg.rtg.world.biome.surface.SurfaceBase;
import teamrtg.rtg.world.biome.terrain.TerrainBase;
import teamrtg.rtg.world.gen.ChunkProviderRTG;

public class RealisticBiomeVanillaFrozenRiver extends RealisticBiomeVanillaBase {

    public RealisticBiomeVanillaFrozenRiver(ChunkProviderRTG chunkProvider) {
        super(
                Biomes.FROZEN_RIVER,
                Biomes.FROZEN_RIVER,
                chunkProvider
        );
    }

    @Override
    protected TerrainBase initTerrain() {
        return new TerrainBase() {
            @Override
            public float generateNoise(OpenSimplexNoise simplex, CellNoise cell, int x, int y, float border, float river) {
                return terrainFlatLakes(x, y, simplex, river, 3f, 60f);
            }
        };
    }


    @Override
    protected void initDecos() {

    }

    @Override
    protected void initProperties() {
        this.config.SURFACE_WATER_LAKE_CHANCE.setDefault(0);
        this.config.SURFACE_LAVA_LAKE_CHANCE.setDefault(0);
    }
}
