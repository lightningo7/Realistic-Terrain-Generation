package rtg.world.biome.realistic.vanilla;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.biome.BiomeConfig;
import rtg.util.*;
import rtg.world.biome.deco.DecoBoulder;
import rtg.world.biome.deco.DecoCactus;
import rtg.world.biome.deco.DecoDeadBush;
import rtg.world.biome.deco.DecoShrub;
import rtg.world.biome.deco.collection.DecoCollectionDesertRiver;
import rtg.world.gen.surface.SurfaceBase;
import rtg.world.gen.terrain.TerrainBase;

public class RealisticBiomeVanillaMesaBryce extends RealisticBiomeVanillaBase {

    public static Biome biome = Biomes.MUTATED_MESA;
    public static Biome river = Biomes.RIVER;

    public RealisticBiomeVanillaMesaBryce(BiomeConfig config) {

        super(config, biome, river);

        this.waterSurfaceLakeChance = 0;
        this.lavaSurfaceLakeChance = 0;
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainVanillaMesaBryce(false, 55f, 120f, 60f, 40f, 69f);
    }

    public class TerrainVanillaMesaBryce extends TerrainBase {

        private float height;
        private float density;
        private float base;

        /*
         * Example parameters:
         *
         * allowed to generate rivers?
         * riverGen = true
         *
         * canyon jump heights
         * heightArray = new float[]{2.0f, 0.5f, 6.5f, 0.5f, 14.0f, 0.5f, 19.0f, 0.5f}
         *
         * strength of canyon jump heights
         * heightStrength = 35f
         *
         * canyon width (cliff to cliff)
         * canyonWidth = 160f
         *
         * canyon heigth (total heigth)
         * canyonHeight = 60f
         *
         * canyon strength
         * canyonStrength = 40f
         *
         */
        public TerrainVanillaMesaBryce(boolean riverGen, float heightStrength, float canyonWidth, float canyonHeight, float canyonStrength, float baseHeight) {
            /**
             * Values come in pairs per layer. First is how high to step up.
             * 	Second is a value between 0 and 1, signifying when to step up.
             */
            height = 20f;
            density = 0.7f;
            base = 69f;
        }

        @Override
        public float generateNoise(OpenSimplexNoise simplex, CellNoise cell, int x, int y, float border, float river) {

            return terrainBryce(x, y, simplex, river, height, border);
        }
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceVanillaMesaBryce(config, BlockUtil.getStateSand(1), BlockUtil.getStateSand(1), 0);
    }

    @Override
    public void rReplace(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base) {

        this.rReplaceRiverSurface(primer, i, j, x, y, depth, world, rand, simplex, cell, noise, river, base);
    }

    @Override
    public int getExtraGoldGenCount() {
        return 20;
    }

    @Override
    public Biome beachBiome() {
        return this.beachBiome(Biomes.BEACH);
    }

    public class SurfaceVanillaMesaBryce extends SurfaceBase {

        private int grassRaise = 0;

        public SurfaceVanillaMesaBryce(BiomeConfig config, IBlockState top, IBlockState fill, int grassHeight) {

            super(config, top, fill);
            grassRaise = grassHeight;
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int z, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base) {

            float c = CliffCalculator.calc(x, z, noise);
            boolean cliff = c > 1.3f;

            for (int k = 255; k > -1; k--) {
                Block b = primer.getBlockState(x, k, z).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                }
                else if (b == Blocks.STONE) {
                    depth++;

                    if (depth > -1 && depth < 12) {
                        if (cliff) {
                            primer.setBlockState(x, k, z, CanyonColour.MESA_BRYCE.getBlockForHeight(i, k, j));
                        }
                        else {
                            if (depth > 4) {
                                primer.setBlockState(x, k, z, CanyonColour.MESA_BRYCE.getBlockForHeight(i, k, j));
                            }
                            else if (k > 74 + grassRaise) {
                                if (rand.nextInt(5) == 0) {
                                    primer.setBlockState(x, k, z, BlockUtil.getStateDirt(1));
                                }
                                else {
                                    if (depth == 0) {
                                        primer.setBlockState(x, k, z, topBlock);
                                    }
                                    else {
                                        primer.setBlockState(x, k, z, fillerBlock);
                                    }
                                }
                            }
                            else if (k < 62) {
                                primer.setBlockState(x, k, z, Blocks.DIRT.getDefaultState());
                            }
                            else if (k < 62 + grassRaise) {
                                if (depth == 0) {
                                    primer.setBlockState(x, k, z, Blocks.GRASS.getDefaultState());
                                }
                                else {
                                    primer.setBlockState(x, k, z, Blocks.DIRT.getDefaultState());
                                }
                            }
                            else if (k < 75 + grassRaise) {
                                if (depth == 0) {
                                    int r = (int) ((k - (62 + grassRaise)) / 2f);
                                    if (rand.nextInt(r + 1) == 0) {
                                        primer.setBlockState(x, k, z, Blocks.GRASS.getDefaultState());
                                    }
                                    else if (rand.nextInt((int) (r / 2f) + 1) == 0) {
                                        primer.setBlockState(x, k, z, BlockUtil.getStateDirt(1));
                                    }
                                    else {
                                        primer.setBlockState(x, k, z, topBlock);
                                    }
                                }
                                else {
                                    primer.setBlockState(x, k, z, fillerBlock);
                                }
                            }
                            else {
                                if (depth == 0) {
                                    primer.setBlockState(x, k, z, topBlock);
                                }
                                else {
                                    primer.setBlockState(x, k, z, fillerBlock);
                                }
                            }
                        }
                    }
                    else if (k > 63) {
                        primer.setBlockState(x, k, z, CanyonColour.MESA_BRYCE.getBlockForHeight(i, k, j));
                    }
                }
            }
        }
    }

    @Override
    public void initDecos() {

        this.addDecoCollection(new DecoCollectionDesertRiver());

        DecoBoulder decoBoulder = new DecoBoulder();
        decoBoulder.boulderBlock = Blocks.COBBLESTONE.getDefaultState();
        decoBoulder.maxY = 83;
        this.addDeco(decoBoulder);

        DecoShrub decoShrub = new DecoShrub();
        decoShrub.loops = 3;
        decoShrub.maxY = 90;
        addDeco(decoShrub);

        DecoDeadBush decoDeadBush = new DecoDeadBush();
        decoDeadBush.maxY = 100;
        decoDeadBush.loops = 3;
        this.addDeco(decoDeadBush);

        DecoCactus decoCactus = new DecoCactus();
        decoCactus.soilBlock = BlockUtil.getStateSand(1);
        decoCactus.loops = 18;
        decoCactus.maxY = 100;
        this.addDeco(decoCactus);
    }
}
