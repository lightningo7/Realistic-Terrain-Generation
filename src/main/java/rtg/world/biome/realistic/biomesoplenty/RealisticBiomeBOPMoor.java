package rtg.world.biome.realistic.biomesoplenty;

import rtg.api.biomes.biomesoplenty.config.BiomeConfigBOPMoor;
import rtg.config.biomesoplenty.ConfigBOP;
import rtg.world.biome.BiomeBase;
import rtg.world.gen.surface.biomesoplenty.SurfaceBOPMoor;
import rtg.world.gen.terrain.biomesoplenty.TerrainBOPMoor;
import biomesoplenty.api.content.BOPCBiomes;

import net.minecraft.block.Block;
import net.minecraft.world.biome.BiomeGenBase;

public class RealisticBiomeBOPMoor extends RealisticBiomeBOPBase
{	
	public static BiomeGenBase bopBiome = BOPCBiomes.moor;
	
	public static Block topBlock = bopBiome.topBlock;
	public static Block fillerBlock = bopBiome.fillerBlock;
	
	public RealisticBiomeBOPMoor()
	{
		super(
			bopBiome, BiomeBase.climatizedBiome(BiomeGenBase.river, Climate.TEMPERATE),
			new TerrainBOPMoor(63f, 69f, 32f),
			new SurfaceBOPMoor(topBlock, fillerBlock)
		);
		
		this.biomeConfig = new BiomeConfigBOPMoor();
		this.biomeWeight = ConfigBOP.weightBOPMoor;
		this.generateVillages = ConfigBOP.villageBOPMoor;
	}
}
