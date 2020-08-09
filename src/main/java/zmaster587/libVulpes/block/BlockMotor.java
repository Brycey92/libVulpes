package zmaster587.libVulpes.block;

import java.util.List;

import zmaster587.libVulpes.api.ITimeModifier;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;

public class BlockMotor extends RotatableBlock implements ITimeModifier {
	
	float timeModifier;
	static final VoxelShape HOLLOW_CUBE = VoxelShapes.create(0.0,0.0,0.0,1.0,1.0,1.0);
	
	public BlockMotor(Properties properties, float timeMultiplier) {
		super(properties);
		timeModifier = timeMultiplier;
	}
	
	@Override
	public float getTimeMult() {
		return timeModifier;
	}
	 
	@Override
	public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(new StringTextComponent(String.format(TextFormatting.GRAY + "Machine Speed: %.2f", 1/getTimeMult())));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return HOLLOW_CUBE;
	}
	
	@Override
	public ItemStack getPickBlock(net.minecraft.block.BlockState state, RayTraceResult target, IBlockReader world,
			BlockPos pos, PlayerEntity player) {
		return super.getPickBlock(state.getBlock().getDefaultState(), target, world, pos, player);
	}
}
