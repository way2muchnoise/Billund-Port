package com.noise.billund.recipe;

import com.noise.billund.init.ModItems;
import com.noise.billund.item.ItemBrick;
import com.noise.billund.reference.MCColour;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class RecipeBrick implements IRecipe {

    @Override
     public boolean matches(InventoryCrafting inv, World world) {
    int bricks = 0;
    int dye = 0;
    for (int i=0; i<inv.getSizeInventory(); i++) {
        ItemStack stack = inv.getStackInSlot(i);
        if (stack != null) {
            if (stack.getItem() == ModItems.brick) {
                bricks++;
            } else if (stack.getItem() == Items.dye) {
                dye++;
            }
        }
    }
    return bricks == 1 && dye == 1;
}

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack brick = null;
        ItemStack component = null;
        boolean dye = false;
        for (int i=0; i<inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null) {
                if (stack.getItem() == ModItems.brick) {
                    brick = stack.copy();
                } else if (stack.getItem() == Items.dye) {
                    component = stack.copy();
                    dye = true;
                }
            }
        }
        if (dye) {
            return ItemBrick.create(
                    MCColour.values()[ItemDye.field_150922_c[component.getItemDamage()]],
                    ItemBrick.getWidth(brick),
                    ItemBrick.getDepth(brick),
                    1
            );
        }
        return null;
    }

    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModItems.brick);
    }
}