/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.block;

import billund.item.ItemBrick;
import billund.reference.Colour;
import billund.reference.Names;
import billund.reference.Textures;
import billund.tileentity.TileEntityBillund;
import billund.util.Brick;
import billund.util.Stud;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockBillund extends BlockContainer implements ITileEntityProvider
{
    public int blockRenderID;
    private static IIcon s_transparentIcon;
    private static IIcon[] s_icons;

    private static Brick s_hoverBrick = null;

    public static void setHoverBrick(Brick brick)
    {
        s_hoverBrick = brick;
    }

    public static IIcon getIcon(int studColour)
    {
        if (studColour >= 0 && studColour < Colour.count())
            return s_icons[studColour];
        return s_transparentIcon;
    }

    public BlockBillund()
    {
        super(Material.wood);
        this.setHardness(0.25f);
        this.setBlockName(Names.Blocks.BILLUND);
        // These get replaced on the client
        blockRenderID = -1;
    }


    @Override
    public int getRenderType()
    {
        return blockRenderID;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public boolean canBeReplacedByLeaves(IBlockAccess world, int x, int y, int z)
    {
        return false;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, int x, int y, int z, int side)
    {
        return false;
    }

    @Override
    public int quantityDropped(Random par1Random)
    {
        return 0;
    }


    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest)
    {
        if (!world.isRemote)
        {
            TileEntity tileEntity = world.getTileEntity(x, y, z);
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
                        entityitem.delayBeforeCanPickup = 30;
                        world.spawnEntityInWorld(entityitem);
                    }

                    // Clear the block
                    if (billund.isEmpty())
                    {
                        world.setBlockToAir(x, y, z);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileEntityBillund)
        {
            TileEntityBillund billund = (TileEntityBillund) tileEntity;
            billund.cullOrphans();
            if (billund.isEmpty()) world.setBlockToAir(x, y, z);
        }
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int i, int j, int k)
    {
        if (s_hoverBrick != null)
        {
            // See if the hovered brick is in the start bit
            int sx = s_hoverBrick.xOrigin;
            int sy = s_hoverBrick.yOrigin;
            int sz = s_hoverBrick.zOrigin;
            {
                int localX = (sx % Stud.ROWS_PER_BLOCK + Stud.ROWS_PER_BLOCK) % Stud.ROWS_PER_BLOCK;
                int localY = (sy % Stud.LAYERS_PER_BLOCK + Stud.LAYERS_PER_BLOCK) % Stud.LAYERS_PER_BLOCK;
                int localZ = (sz % Stud.ROWS_PER_BLOCK + Stud.ROWS_PER_BLOCK) % Stud.ROWS_PER_BLOCK;
                int blockX = (sx - localX) / Stud.ROWS_PER_BLOCK;
                int blockY = (sy - localY) / Stud.LAYERS_PER_BLOCK;
                int blockZ = (sz - localZ) / Stud.ROWS_PER_BLOCK;

                if ((i == blockX || i == blockX + 1) &&
                        (j == blockY || j == blockY + 1) &&
                        (k == blockZ || k == blockZ + 1))
                {
                    float xScale = 1.0f / (float) Stud.ROWS_PER_BLOCK;
                    float yScale = 1.0f / (float) Stud.LAYERS_PER_BLOCK;
                    float zScale = 1.0f / (float) Stud.ROWS_PER_BLOCK;

                    float startX = (float) (sx - (i * Stud.ROWS_PER_BLOCK)) * xScale;
                    float startY = (float) (sy - (j * Stud.LAYERS_PER_BLOCK)) * yScale;
                    float startZ = (float) (sz - (k * Stud.ROWS_PER_BLOCK)) * zScale;
                    this.setBlockBounds(
                            startX, startY, startZ,
                            startX + (float) s_hoverBrick.width * xScale,
                            startY + (float) s_hoverBrick.height * yScale,
                            startZ + (float) s_hoverBrick.depth * zScale
                    );
                    return;
                }
            }
        }

        // Set bounds to something that should hopefully never be hit
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getBoundingBox((double) par2 + this.minX, (double) par3 + this.minY, (double) par4 + this.minZ, (double) par2 + this.maxX, (double) par3 + this.maxY, (double) par4 + this.maxZ);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return AxisAlignedBB.getBoundingBox((double) par2 + this.minX, (double) par3 + this.minY, (double) par4 + this.minZ, (double) par2 + this.maxX, (double) par3 + this.maxY, (double) par4 + this.maxZ);
    }

    @Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB bigBox, List list, Entity entity)
    {
        TileEntity tileEntity = world.getTileEntity(i, j, k);
        if (tileEntity != null && tileEntity instanceof TileEntityBillund)
        {
            double originX = (double) i;
            double originY = (double) j;
            double originZ = (double) k;
            double stepX = 1.0 / (double) Stud.STUDS_PER_ROW;
            double stepY = 1.0 / (double) Stud.STUDS_PER_COLUMN;
            double stepZ = 1.0 / (double) Stud.STUDS_PER_ROW;

            int minsx = i * Stud.STUDS_PER_ROW;
            int minsy = j * Stud.STUDS_PER_COLUMN;
            int minsz = k * Stud.STUDS_PER_ROW;

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
                                AxisAlignedBB littleBox = AxisAlignedBB.getBoundingBox(
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
                                    AxisAlignedBB littleBox = AxisAlignedBB.getBoundingBox(
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
    public IIcon getIcon(int side, int damage)
    {
        return s_transparentIcon;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int id)
    {
        return new TileEntityBillund();
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        s_icons = new IIcon[Colour.count()];
        s_transparentIcon = iconRegister.registerIcon(Textures.RESOURCE_PREFIX + Textures.COLOURS_PREFIX + "transparent");
        for (Colour colour : Colour.values())
            if (colour.number >= 0)
                s_icons[colour.number] = iconRegister.registerIcon(Textures.RESOURCE_PREFIX + Textures.COLOURS_PREFIX + colour.name);
    }
}
