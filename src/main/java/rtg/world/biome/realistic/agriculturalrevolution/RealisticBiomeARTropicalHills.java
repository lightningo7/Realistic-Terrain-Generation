package rtg.world.biome.realistic.agriculturalrevolution;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;

import rtg.api.biome.BiomeConfig;
import rtg.util.CellNoise;
import rtg.util.CliffCalculator;
import rtg.util.OpenSimplexNoise;
import rtg.world.biome.deco.DecoBaseBiomeDecorations;
import rtg.world.gen.surface.SurfaceBase;
import rtg.world.gen.terrain.TerrainBase;

public class RealisticBiomeARTropicalHills extends RealisticBiomeARBase {

    public static Biome river = Biomes.RIVER;

    public RealisticBiomeARTropicalHills(Biome biome, BiomeConfig config) {

        super(config, biome, river);
    }

    @Override
    public TerrainBase initTerrain() {

        return new TerrainARTropicalHills(10f, 80f, 68f, 200f);
    }

    public class TerrainARTropicalHills extends TerrainBase {

        private float start;
        private float height;
        private float width;

        public TerrainARTropicalHills(float hillStart, float landHeight, float baseHeight, float hillWidth) {

            start = hillStart;
            height = landHeight;
            base = baseHeight;
            width = hillWidth;
        }

        @Override
        public float generateNoise(OpenSimplexNoise simplex, CellNoise cell, int x, int y, float border, float river)
        {
            return terrainHighland(x, y, simplex, cell, river, start, width, height, base - 62f);
        }
    }

    @Override
    public SurfaceBase initSurface() {

        return new SurfaceARTropicalHills(config, Blocks.SAND.getDefaultState(), Blocks.SANDSTONE.getDefaultState(), false, null, 0f, 1.5f, 60f, 65f, 1.5f);
    }

    public class SurfaceARTropicalHills extends SurfaceBase {

        private boolean beach;
        private IBlockState beachBlock;
        private float min;

        private float sCliff = 1.5f;
        private float sHeight = 60f;
        private float sStrength = 65f;
        private float cCliff = 1.5f;

        public SurfaceARTropicalHills(BiomeConfig config, IBlockState top, IBlockState fill, boolean genBeach, IBlockState genBeachBlock, float minCliff) {

            super(config, top, fill);
            beach = genBeach;
            beachBlock = genBeachBlock;
            min = minCliff;
        }

        public SurfaceARTropicalHills(BiomeConfig config, IBlockState top, IBlockState fill, boolean genBeach, IBlockState genBeachBlock, float minCliff, float stoneCliff, float stoneHeight, float stoneStrength, float clayCliff) {

            this(config, top, fill, genBeach, genBeachBlock, minCliff);

            sCliff = stoneCliff;
            sHeight = stoneHeight;
            sStrength = stoneStrength;
            cCliff = clayCliff;
        }

        @Override
        public void paintTerrain(ChunkPrimer primer, int i, int j, int x, int y, int depth, World world, Random rand, OpenSimplexNoise simplex, CellNoise cell, float[] noise, float river, Biome[] base) {

            float c = CliffCalculator.calc(x, y, noise);
            int cliff = 0;
            boolean gravel = false;

            Block b;
            for (int k = 255; k > -1; k--) {
                b = primer.getBlockState(x, k, y).getBlock();
                if (b == Blocks.AIR) {
                    depth = -1;
                }
                else if (b == Blocks.STONE) {
                    depth++;

                    if (depth == 0) {
                        if (k < 63) {
                            if (beach) {
                                gravel = true;
                            }
                        }

                        float p = simplex.noise3(i / 8f, j / 8f, k / 8f) * 0.5f;
                        if (c > min && c > sCliff - ((k - sHeight) / sStrength) + p) {
                            cliff = 1;
                        }
                        if (c > cCliff) {
                            cliff = 2;
                        }

                        if (cliff == 1) {
                            primer.setBlockState(x, k, y, Blocks.SANDSTONE.getDefaultState());
                        }
                        else if (cliff == 2) {
                            primer.setBlockState(x, k, y, Blocks.SANDSTONE.getDefaultState());
                        }
                        else if (k < 63) {
                            if (beach) {
                                primer.setBlockState(x, k, y, beachBlock);
                                gravel = true;
                            }
                            else if (k < 62) {
                                primer.setBlockState(x, k, y, fillerBlock);
                            }
                            else {
                                primer.setBlockState(x, k, y, topBlock);
                            }
                        }
                        else {
                            primer.setBlockState(x, k, y, topBlock);
                        }
                    }
                    else if (depth < 6) {
                        if (cliff == 1) {
                            primer.setBlockState(x, k, y, Blocks.SANDSTONE.getDefaultState());
                        }
                        else if (cliff == 2) {
                            primer.setBlockState(x, k, y, Blocks.SANDSTONE.getDefaultState());
                        }
                        else if (gravel) {
                            primer.setBlockState(x, k, y, beachBlock);
                        }
                        else {
                            primer.setBlockState(x, k, y, fillerBlock);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void initDecos() {

        DecoBaseBiomeDecorations decoBaseBiomeDecorations = new DecoBaseBiomeDecorations();
        this.addDeco(decoBaseBiomeDecorations);
    }
}
