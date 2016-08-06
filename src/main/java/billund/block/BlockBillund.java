/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.block;

import billund.item.ItemBrick;
import billund.reference.Colour;
import billund.reference.Names;
import billund.tileentity.TileEntityBillund;
import billund.util.Brick;
import billund.util.Stud;;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class BlockBillund extends BlockContainer implements ITileEntityProvider
{
    public int blockRenderID;

    private static Brick s_hoverBrick = null;

    public static void setHoverBrick(Brick brick)
    {
        s_hoverBrick = brick;
    }

    public BlockBillund()
    {
        super(Material.WOOD);
        this.setHardness(0.25f);
        this.setRegistryName(Names.Blocks.BILLUND);
        // These get replaced on the client
        blockRenderID = -1;
    }


    @Override
    public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
    {
        return false;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }


    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest)
    {
        if (!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(pos);
            if (tileEntity != null && tileEntity instanceof TileEntityBillund)
            {
                // Find a brick to destroy
                TileEntityBillund billund = (TileEntityBillund) tileEntity;
                Brick brick = ItemBrick.getExistingBrick(world, player, 1.0f);
                if (brick != null)
                {
                    // Remove the brick
                    Stud.removeBrick(world, brick);

                    // Spawn an item for the destroyed brick
                    if (!player.capabilities.isCreativeMode)
                    {
                        float brickX = ((float) brick.xOrigin + (float) brick.width * 0.5f) / (float) Stud.ROWS_PER_BLOCK;
                        float brickY = ((float) brick.yOrigin + (float) brick.height) / (float) Stud.LAYERS_PER_BLOCK;
                        float brickZ = ((float) brick.zOrigin + (float) brick.depth * 0.5f) / (float) Stud.ROWS_PER_BLOCK;
                        ItemStack stack = ItemBrick.create(brick.colour, Math.min(brick.width, brick.depth), Math.max(brick.width, brick.depth), 1);
                        EntityItem entityitem = new EntityItem(world, brickX, brickY + 0.05f, brickZ, stack);
                        entityitem.motionX = 0.0f;
                        entityitem.motionY = 0.0f;
                        entityitem.motionZ = 0.0f;
                        entityitem.setPickupDelay(30);
                        world.spawnEntityInWorld(entityitem);
                    }

                    // Clear the block
                    if (billund.isEmpty())
                    {
                        world.setBlockToAir(pos);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityBillund)
        {
            TileEntityBillund billund = (TileEntityBillund) tileEntity;
            billund.cullOrphans();
            if (billund.isEmpty() && world instanceof World) ((World) world).setBlockToAir(pos);
        }
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB bigBox, List<AxisAlignedBB> list, @Nullable Entity entityIn)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityBillund)
        {
            double originX = (double) pos.getX();
            double originY = (double) pos.getY();
            double originZ = (double) pos.getZ();
            double stepX = 1.0 / (double) Stud.STUDS_PER_ROW;
            double stepY = 1.0 / (double) Stud.STUDS_PER_COLUMN;
            double stepZ = 1.0 / (double) Stud.STUDS_PER_ROW;

            int minsx = pos.getX() * Stud.STUDS_PER_ROW;
            int minsy = pos.getY() * Stud.STUDS_PER_COLUMN;
            int minsz = pos.getZ() * Stud.STUDS_PER_ROW;

            TileEntityBillund billund = (TileEntityBillund) tileEntity;
            for (int x = 0; x < Stud.STUDS_PER_ROW; ++x)
            {
                for (int y = 0; y < Stud.STUDS_PER_COLUMN; ++y)
                {
                    for (int z = 0; z < Stud.STUDS_PER_ROW; ++z)
                    {
                        Stud stud = billund.getStudLocal(x, y, z);
                        if (stud != null)
                        {
                            double startX = originX + (double) x * stepX;
                            double startY = originY + (double) y * stepY;
                            double startZ = originZ + (double) z * stepZ;
                            if (stud.XOrigin < minsx || stud.YOrigin < minsy || stud.ZOrigin < minsz)
                            {
                                // If the origin of this brick is in a different block, add our own aabbs for each stud
                                AxisAlignedBB littleBox = new AxisAlignedBB(
                                        startX, startY, startZ,
                                        startX + stepX,
                                        startY + stepY,
                                        startZ + stepZ
                                );
                                if (littleBox.intersectsWith(bigBox))
                                    list.add(littleBox);
                            } else
                            {
                                // Else, if this stud *is* the origin, add an aabb for the whole thing
                                int sx = x + minsx;
                                int sy = y + minsy;
                                int sz = z + minsz;
                                if (sx == stud.XOrigin && sy == stud.YOrigin && sz == stud.ZOrigin)
                                {
                                    AxisAlignedBB littleBox = new AxisAlignedBB(
                                            startX, startY, startZ,
                                            startX + (double) stud.BrickWidth * stepX,
                                            startY + (double) stud.BrickHeight * stepY,
                                            startZ + (double) stud.BrickDepth * stepZ
                                    );
                                    if (littleBox.intersectsWith(bigBox))
                                        list.add(littleBox);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int id)
    {
        return new TileEntityBillund();
    }
}
