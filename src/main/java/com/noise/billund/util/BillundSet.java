/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.noise.billund.util;

import com.noise.billund.item.ItemBrick;
import com.noise.billund.reference.Colour;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class BillundSet {
    public static BillundSet get(int index) {
        return new BillundSet(index);
    }

    private static String[] s_setNames = new String[]{
            "Starter Pack",
            "Colour Pack A",
            "Colour Pack B",
            "Colour Pack C",
            "Colour Pack D",
    };

    private static int[] s_setCosts = new int[]{
            7,
            10,
            10,
            10,
            10
    };

    private int m_index;

    public BillundSet(int index) {
        m_index = index;
    }

    public int getCost() {
        return s_setCosts[m_index];
    }

    public String getDescription() {
        return s_setNames[m_index];
    }

    private IInventory s_addInventory = null;
    private int s_addIndex = 0;

    public void populateChest(IInventory inv) {
        s_addIndex = 0;
        s_addInventory = inv;

        switch (m_index) {
            case 0: {
                // Starter set
                // Basic pieces in 6 colours
                addBasic(Colour.RED);
                addBasic(Colour.GREEN);
                add(null);

                addBasic(Colour.BLUE);
                addBasic(Colour.YELLOW);
                add(null);

                addBasic(Colour.WHITE);
                addBasic(Colour.BLACK);
                add(null);
                break;
            }
            case 1: {
                // Colour Pack
                // pieces in 3 colours
                addAll(Colour.RED);
                addAll(Colour.GREEN);
                addAll(Colour.BLUE);
                break;
            }
            case 2: {
                // Colour Pack
                // pieces in 3 colours
                addAll(Colour.ORANGE);
                addAll(Colour.YELLOW);
                addAll(Colour.LIME);
                break;
            }
            case 3: {
                // Colour Pack
                // pieces in 3 colours
                addAll(Colour.PINK);
                addAll(Colour.PURPLE);
                addAll(Colour.WHITE);
                break;
            }
            case 4: {
                // Colour Pack
                // pieces in 3 colours
                addAll(Colour.LIGHT_GREY);
                addAll(Colour.GREY);
                addAll(Colour.BLACK);
                break;
            }
        }
    }

    private void add(ItemStack stack) {
        int slot = s_addIndex++;
        if (slot < s_addInventory.getSizeInventory()) {
            s_addInventory.setInventorySlotContents(slot, stack);
        }
    }

    private void addBasic(Colour colour) {
        add(ItemBrick.create(colour, 1, 2, 24));
        add(ItemBrick.create(colour, 1, 4, 24));
        add(ItemBrick.create(colour, 2, 2, 24));
        add(ItemBrick.create(colour, 2, 4, 24));
    }

    private void addAll(Colour colour) {
        add(ItemBrick.create(colour, 1, 1, 24));
        add(ItemBrick.create(colour, 1, 2, 24));
        add(ItemBrick.create(colour, 1, 3, 24));
        add(ItemBrick.create(colour, 1, 4, 24));
        add(ItemBrick.create(colour, 1, 6, 24));
        add(ItemBrick.create(colour, 2, 2, 24));
        add(ItemBrick.create(colour, 2, 3, 24));
        add(ItemBrick.create(colour, 2, 4, 24));
        add(ItemBrick.create(colour, 2, 6, 24));
    }
}
