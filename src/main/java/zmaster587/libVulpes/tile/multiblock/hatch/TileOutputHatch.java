package zmaster587.libVulpes.tile.multiblock.hatch;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import zmaster587.libVulpes.api.LibVulpesTileEntityTypes;
import zmaster587.libVulpes.inventory.modules.ModuleBase;
import zmaster587.libVulpes.inventory.modules.ModuleOutputSlotArray;

public class TileOutputHatch extends TileInventoryHatch {

	

	public TileOutputHatch() {
		super(LibVulpesTileEntityTypes.TILE_OUTPUT_HATCH);
	}

	public TileOutputHatch(int size) {
		super(LibVulpesTileEntityTypes.TILE_OUTPUT_HATCH, size);
	}

	@Override
	public String getModularInventoryName() {
		return "block.libvulpes.hatch.1";
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn,
			Direction direction) {
		return false;
	}

	@Override
	public List<ModuleBase> getModules(int ID, PlayerEntity player) {
		LinkedList<ModuleBase> modules = new LinkedList<ModuleBase>();
		modules.add(new ModuleOutputSlotArray(8, 18, this, 0, this.getSizeInventory()));
		return modules;
	}
	
	
}
