package zmaster587.libVulpes.util;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FluidUtils {
	
	private static Map<String, List<String>> fluidEquivalentMapping = new HashMap<>();

	public static boolean containsFluid(@Nonnull ItemStack stack) {
		return !stack.isEmpty() && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, EnumFacing.UP);
	}

	public static boolean containsFluid(@Nonnull ItemStack stack, Fluid fluid) {
		if(containsFluid(stack)) {
			IFluidHandlerItem fluidItem = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, EnumFacing.UP);

			if(fluidItem != null) {
				FluidStack fluidStack = fluidItem.getTankProperties()[0].getContents();
				return fluidStack != null && areFluidsSameType(fluidStack.getFluid(), fluid);
			}
		}

		return false;
	}

	public static int getFluidItemCapacity(@Nonnull ItemStack stack) {
		if(containsFluid(stack)) {
			IFluidHandlerItem fluidItem = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, EnumFacing.UP);

			if(fluidItem != null)
				return  fluidItem.getTankProperties()[0].getCapacity();
		}
		return 0;
	}

	public static IFluidHandlerItem getFluidHandler(@Nonnull ItemStack stack) {
		return stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, EnumFacing.UP);
	}

	public static FluidStack getFluidForItem(@Nonnull ItemStack item) {
		if(!containsFluid(item))
			return null;
		IFluidHandlerItem fluidItem = item.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, EnumFacing.UP);

		return fluidItem == null ? null : fluidItem.getTankProperties()[0].getContents();
	}

	public static boolean shouldFillContainer(@Nonnull ItemStack stack, IFluidTankProperties tankProperty) {
		FluidStack fStack = getFluidForItem(stack);
		if(fStack == null || tankProperty.getContents() == null) {
			return false;
		}

		int fStackCapacity = getFluidItemCapacity(stack);
		int tankContentsAmt = tankProperty.getContents().amount;

		return (stack.getItem() == Items.BUCKET ||
				(fStackCapacity != 1000 && fStack.amount > 0 && tankContentsAmt > 0) ||
				(fStack.amount == 0 && tankContentsAmt > 0) ||
				(fStack.amount < fStackCapacity && tankContentsAmt == tankProperty.getCapacity()));
	}

	//Use Forge methods to handle containers being put into inventories
	public static boolean attemptDrainContainerIInv(EmbeddedInventory inv, IFluidHandler tank, @Nonnull ItemStack stack, int inputSlot, int outputSlot) {
		if (containsFluid(stack)) {
			boolean fill = false;
			boolean toReturn = false;
			FluidActionResult modifiedContainer;
			FluidStack fStack = getFluidForItem(stack);
			IFluidTankProperties tankProperty =  tank.getTankProperties()[0];

			if(fStack == null || tankProperty.getContents() == null) {
				return false;
			}

			if (shouldFillContainer(stack, tankProperty)) {
				modifiedContainer = FluidUtil.tryFillContainer(stack, tank, getFluidItemCapacity(stack), null, false);
				fill = true;
			} else {
				modifiedContainer = FluidUtil.tryEmptyContainer(stack, tank, getFluidItemCapacity(stack), null, false);
			}
				if (modifiedContainer.isSuccess()) {
					if (inv.getStackInSlot(outputSlot).isEmpty()) {
						toReturn = fill ? FluidUtil.tryFillContainer(stack, tank, getFluidItemCapacity(stack), null, true).isSuccess() : FluidUtil.tryEmptyContainer(stack, tank, getFluidItemCapacity(stack), null, true).isSuccess();
						inv.getStackInSlot(inputSlot).shrink(1);
						inv.setStackInSlot(outputSlot, modifiedContainer.getResult());
					} else if ((inv.getStackInSlot(outputSlot).getItem() == modifiedContainer.getResult().getItem() && inv.getStackInSlot(outputSlot).getCount() < inv.getStackInSlot(outputSlot).getMaxStackSize())) {
						toReturn = fill ? FluidUtil.tryFillContainer(stack, tank, getFluidItemCapacity(stack), null, true).isSuccess() : FluidUtil.tryEmptyContainer(stack, tank, getFluidItemCapacity(stack), null, true).isSuccess();
						inv.getStackInSlot(inputSlot).shrink(1);
						inv.getStackInSlot(outputSlot).grow(1);
						return true;
					}
				}
                return toReturn;
			}
		return false;
	}
	
	public static void addFluidMapping(Fluid in, String altName)
	{
		String fluidKeyName = in.getName();
		addFluidMapping(fluidKeyName, altName);
		addFluidMapping(altName, fluidKeyName);
	}
	
	private static void addFluidMapping(String in, String altName)
	{
		List<String> mappedValues;
		if(!fluidEquivalentMapping.containsKey(in))
		{
			mappedValues = new LinkedList<>();
			fluidEquivalentMapping.put(in, mappedValues);
		}
		else
			mappedValues = fluidEquivalentMapping.get(in);
		
		mappedValues.add(altName);
	}
	
	public static boolean areFluidsSameType(Fluid in, Fluid otherFluid) {
		if(in == null || otherFluid == null)
			return false;
		String inFluidName = in.getName();
		String otherFluidName = otherFluid.getName();
		
		if(inFluidName.equals(otherFluidName))
			return true;
		
		return fluidEquivalentMapping.containsKey(inFluidName) && fluidEquivalentMapping.get(inFluidName).contains(otherFluidName);
	}
}
