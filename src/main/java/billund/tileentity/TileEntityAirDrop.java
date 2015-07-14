/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.tileentity;

import billund.handler.ConfigHandler;
import billund.registry.BillundSetRegistry;
import billund.set.BillundSet;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class TileEntityAirDrop extends Entity
{
    public Block block;
    public int metadata;
    public BillundSet set;
    public boolean deployed;

    public TileEntityAirDrop(World world)
    {
        super(world);
        this.block = Blocks.chest;
        this.metadata = 0;
    }

    public TileEntityAirDrop(World world, double x, double y, double z, BillundSet set)
    {
        this(world);
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.98f);
        this.yOffset = this.height / 2.0f;
        this.setPosition(x, y, z);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.set = set;
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected void entityInit()
    {
    }

    @Override
    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.deployed)
        {
            float deployHeight = (float) (this.worldObj.getTopSolidOrLiquidBlock(
                    MathHelper.floor_double(this.posX),
                    MathHelper.floor_double(this.posZ)
            )) + 11.0f;
            if (this.posY <= deployHeight)
                this.deployed = true;
        }

        if (!this.deployed)
            this.motionY -= 0.003 * ConfigHandler.speedMultiplier;
        else
            this.motionY -= 0.02 * ConfigHandler.speedMultiplier;

        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.98;
        this.motionY *= 0.98;
        this.motionZ *= 0.98;

        if (!this.worldObj.isRemote)
        {
            int blockX = MathHelper.floor_double(this.posX);
            int blockY = MathHelper.floor_double(this.posY);
            int blockZ = MathHelper.floor_double(this.posZ);

            if (this.onGround)
            {
                this.motionX *= 0.7;
                this.motionZ *= 0.7;
                this.motionY *= -0.5;

                if (this.worldObj.getBlock(blockX, blockY, blockZ) != Blocks.piston_extension)
                {
                    this.setDead();

                    // Set the block
                    this.worldObj.setBlock(blockX, blockY, blockZ, this.block, this.metadata, 3);

                    //particles
                    this.worldObj.playAuxSFX(2006, blockX, blockY - 1, blockZ, 10);

                    // Populate the block
                    TileEntity entity = this.worldObj.getTileEntity(blockX, blockY, blockZ);
                    if (entity != null && entity instanceof TileEntityChest)
                    {
                        TileEntityChest inv = (TileEntityChest) entity;
                        //rename the chest to corresponding set
                        inv.func_145976_a(this.set.getLocalizedName());
                        this.set.populateChest(inv);
                        inv.markDirty();
                    }
                }
            } else if (blockY < 0)
                this.setDead();
        }
    }

    @Override
    protected void fall(float par1)
    {
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbtTagCompound)
    {
        nbtTagCompound.setByte("Data", (byte) this.metadata);
        nbtTagCompound.setString("Set", this.set.getName());
        nbtTagCompound.setBoolean("Deployed", this.deployed);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbtTagCompound)
    {
        this.metadata = nbtTagCompound.getByte("Data") & 255;
        this.set = BillundSetRegistry.instance().getBillundSet(nbtTagCompound.getString("Set"));
        this.deployed = nbtTagCompound.getBoolean("Deployed");
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0f;
    }

    @SideOnly(Side.CLIENT)
    public World getWorld()
    {
        return this.worldObj;
    }

    @SideOnly(Side.CLIENT)
    public boolean canRenderOnFire()
    {
        return false;
    }
}
