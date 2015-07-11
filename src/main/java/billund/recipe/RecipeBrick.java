package billund.recipe;

import billund.handler.ConfigHandler;
import billund.registry.ItemRegistry;
import billund.item.ItemBrick;
import billund.reference.Colour;
import billund.util.OreDictionaryHelper;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeBrick implements IRecipe
{

    @Override
    public boolean matches(InventoryCrafting inv, World world)
    {
        int bricks = 0;
        int dye = 0;
        boolean other = false;
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null)
            {
                if (stack.getItem() == ItemRegistry.brick)
                    bricks++;
                else if (OreDictionaryHelper.compareOre(stack, "dye"))
                    dye++;
                else
                    other = true;
            }
        }
        return bricks == 1 && dye == 1 && !other && ConfigHandler.redye;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack brick = null;
        ItemStack component = null;
        boolean dye = false;
        for (int i = 0; i < inv.getSizeInventory(); i++)
        {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null)
            {
                if (stack.getItem() == ItemRegistry.brick)
                    brick = stack.copy();
                else if (OreDictionaryHelper.compareOre(stack, "dye"))
                    component = stack.copy();
                    dye = true;
            }
        }
        if (dye)
        {
            String name = null; // TODO: get some name here?
            return ItemBrick.create(
                    Colour.getNamed(name),
                    ItemBrick.getWidth(brick),
                    ItemBrick.getDepth(brick),
                    1
            );
        }
        return null;
    }

    @Override
    public int getRecipeSize()
    {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput()
    {
        return new ItemStack(ItemRegistry.brick);
    }
}