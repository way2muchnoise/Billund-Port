/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.noise.billund.proxy;

import com.noise.billund.Billund;
import com.noise.billund.init.ModItems;
import com.noise.billund.network.MessageHandler;
import com.noise.billund.tileentity.TileEntityAirDrop;
import com.noise.billund.tileentity.TileEntityBillund;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Random;

public abstract class CommonProxy implements IProxy {
    // IProxy implementation

    public abstract void openOrderFormGUI(EntityPlayer player);

    public void registerEntities() {
        // airdrop entity
        EntityRegistry.registerModEntity(TileEntityAirDrop.class, "airDrop", 1, Billund.instance, 80, 3, true);
    }

    public void registerTileEntities() {
        // Tile Entities
        GameRegistry.registerTileEntity(TileEntityBillund.class, "billund");
    }

    public void registerHandlers() {
        MinecraftForge.EVENT_BUS.register(new ForgeHandlers());
        MessageHandler.init();
    }

    public class ForgeHandlers {
        private Random r = new Random();

        // Forge event responses

        @SubscribeEvent
        public void onEntityLivingDeath(LivingDeathEvent event) {
            if (event.entity.worldObj.isRemote) {
                return;
            }

            if (event.entity instanceof EntityZombie) {
                EntityLivingBase living = (EntityLivingBase) event.entity;
                if ((living.isChild() && r.nextInt(20) == 0) ||
                        (!living.isChild() && r.nextInt(100) == 0)) {
                    event.entity.entityDropItem(new ItemStack(ModItems.orderForm, 1), 0.0f);
                }
            }
        }
    }
}
