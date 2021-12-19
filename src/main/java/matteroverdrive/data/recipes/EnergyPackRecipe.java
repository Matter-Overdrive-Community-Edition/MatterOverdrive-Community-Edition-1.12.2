
package matteroverdrive.data.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;

public class EnergyPackRecipe extends ShapelessRecipes {
    //TODO: this could probably be turned into a json recipe with custom logic
    public EnergyPackRecipe(String group, ItemStack output, NonNullList<Ingredient> ingredients) {
        super(group, output, ingredients);
    }
/*
	public EnergyPackRecipe(ItemStack... recipeitems)
	{
		super(new ItemStack(MatterOverdrive.items.energyPack), Arrays.asList(recipeitems));
		for (ItemStack stack : recipeItems)
		{
			if (!stack.isEmpty() && stack.getItem() instanceof Battery)
			{

				((Battery)stack.getItem()).setEnergyStored(stack, ((Battery)stack.getItem()).getMaxEnergyStored(stack));
				getRecipeOutput().stackSize = ((Battery)stack.getItem()).getEnergyStored(stack) / MatterOverdrive.items.energyPack.getEnergyAmount(getRecipeOutput());
			}
		}
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inventoryCrafting)
	{
		ItemStack stack = getRecipeOutput().copy();
		for (int i = 0; i < inventoryCrafting.getSizeInventory(); i++)
		{
			if (inventoryCrafting.getStackInSlot(i) != null && inventoryCrafting.getStackInSlot(i).getItem() instanceof IEnergyContainerItem)
			{
				int energyStored = ((IEnergyContainerItem)inventoryCrafting.getStackInSlot(i).getItem()).getEnergyStored(inventoryCrafting.getStackInSlot(i));
				int packEnergy = MatterOverdrive.items.energyPack.getEnergyAmount(inventoryCrafting.getStackInSlot(i));
				if (energyStored > 0)
				{
					stack.getCount() = energyStored / packEnergy;
					return stack;
				}
			}
		}
		return null;
	}*/
}
