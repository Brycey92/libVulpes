package zmaster587.libVulpes.api;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import zmaster587.libVulpes.tile.TilePointer;
import zmaster587.libVulpes.tile.TileSchematic;
import zmaster587.libVulpes.tile.energy.TileCoalGenerator;
import zmaster587.libVulpes.tile.energy.TileCreativePowerInput;
import zmaster587.libVulpes.tile.energy.TileForgePowerInput;
import zmaster587.libVulpes.tile.energy.TileForgePowerOutput;
import zmaster587.libVulpes.tile.multiblock.TilePlaceholder;
import zmaster587.libVulpes.tile.multiblock.hatch.TileFluidHatch;
import zmaster587.libVulpes.tile.multiblock.hatch.TileInputHatch;
import zmaster587.libVulpes.tile.multiblock.hatch.TileOutputHatch;

public class LibVulpesTileEntityTypes {

	public static TileEntityType<?> TILE_OUTPUT_HATCH;
	public static TileEntityType<?> TILE_INPUT_HATCH;
	public static TileEntityType<?> TILE_PLACEHOLDER;
	public static TileEntityType<?> TILE_FLUIDHATCH;
	public static TileEntityType<?> TILE_SCHEMATIC;
	public static TileEntityType<?> TILE_CREATIVE_BATTERY;
	public static TileEntityType<?> TILE_FORGE_POWER_INPUT;
	public static TileEntityType<?> TILE_FORGE_POWER_OUTPUT;
	public static TileEntityType<?> TILE_COAL_GENERATOR;
	public static TileEntityType<?> TILE_POINTER;
	
	
	public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> evt)
	{
		TILE_OUTPUT_HATCH = TileEntityType.Builder.create(TileOutputHatch::new, LibVulpesBlocks.blockHatch).build(null).setRegistryName("outputhatch");
		TILE_INPUT_HATCH = TileEntityType.Builder.create(TileInputHatch::new, LibVulpesBlocks.blockHatch).build(null).setRegistryName("inputhatch");
		TILE_PLACEHOLDER = TileEntityType.Builder.create(TilePlaceholder::new, LibVulpesBlocks.blockPlaceHolder).build(null).setRegistryName("placeholder");
		TILE_FLUIDHATCH = TileEntityType.Builder.create(TileFluidHatch::new, LibVulpesBlocks.blockHatch).build(null).setRegistryName("fluidhatch");
		TILE_SCHEMATIC = TileEntityType.Builder.create(TileSchematic::new, LibVulpesBlocks.blockPhantom).build(null).setRegistryName("schematic");
		TILE_CREATIVE_BATTERY = TileEntityType.Builder.create(TileCreativePowerInput::new, LibVulpesBlocks.blockCreativeInputPlug).build(null).setRegistryName("creativebattery");
		TILE_FORGE_POWER_INPUT = TileEntityType.Builder.create(TileForgePowerInput::new, LibVulpesBlocks.blockForgeInputPlug).build(null).setRegistryName("forgepowerinput");
		TILE_FORGE_POWER_OUTPUT = TileEntityType.Builder.create(TileForgePowerOutput::new, LibVulpesBlocks.blockForgeOutputPlug).build(null).setRegistryName("forgepoweroutput");
		TILE_COAL_GENERATOR = TileEntityType.Builder.create(TileCoalGenerator::new, LibVulpesBlocks.blockCoalGenerator).build(null).setRegistryName("coalgenerator");
		TILE_POINTER = TileEntityType.Builder.create(TilePointer::new, LibVulpesBlocks.blockCoalGenerator).build(null).setRegistryName("pointer");
		
		
		IForgeRegistry<TileEntityType<?>> r = evt.getRegistry();
		r.register(LibVulpesTileEntityTypes.TILE_INPUT_HATCH);
		r.register(LibVulpesTileEntityTypes.TILE_OUTPUT_HATCH);
		r.register(LibVulpesTileEntityTypes.TILE_PLACEHOLDER);
		r.register(LibVulpesTileEntityTypes.TILE_FLUIDHATCH);
		r.register(LibVulpesTileEntityTypes.TILE_SCHEMATIC);
		r.register(LibVulpesTileEntityTypes.TILE_CREATIVE_BATTERY);
		r.register(LibVulpesTileEntityTypes.TILE_FORGE_POWER_INPUT);
		r.register(LibVulpesTileEntityTypes.TILE_FORGE_POWER_OUTPUT);
		r.register(LibVulpesTileEntityTypes.TILE_COAL_GENERATOR);
		
	}
}
