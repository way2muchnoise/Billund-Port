/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.util;

import billund.reference.Colour;
import billund.registry.BlockRegistry;
import billund.tileentity.TileEntityBillund;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class Stud
{
    public Colour Colour;
    public int XOrigin;
    public int YOrigin;
    public int ZOrigin;
    public int BrickWidth;
    public int BrickHeight;
    public int BrickDepth;

    public static final int ROWS_PER_BLOCK = 6;
    public static final int LAYERS_PER_BLOCK = 5;

    public static final int STUDS_PER_ROW = ROWS_PER_BLOCK;
    public static final int STUDS_PER_COLUMN = LAYERS_PER_BLOCK;
    public static final int STUDS_PER_LAYER = ROWS_PER_BLOCK * ROWS_PER_BLOCK;
    public static final int STUDS_PER_BLOCK = LAYERS_PER_BLOCK * STUDS_PER_LAYER;

    public Stud()
    {
    }

    public Stud(Colour colour, int xOrigin, int yOrigin, int zOrigin, int width, int height, int depth)
    {
        Colour = colour;
        XOrigin = xOrigin;
        YOrigin = yOrigin;
        ZOrigin = zOrigin;
        BrickWidth = width;
        BrickHeight = height;
        BrickDepth = depth;
    }

    public Stud(Brick brick, int xLocal, int yLocal, int zLocal)
    {
        this(brick.colour, brick.xOrigin, brick.yOrigin, brick.zOrigin, brick.width, brick.height, brick.depth);
    }

    public static Stud getStud(IBlockAccess world, int x, int y, int z)
    {
        int localX = (x % ROWS_PER_BLOCK + ROWS_PER_BLOCK) % ROWS_PER_BLOCK;
        int localY = (y % LAYERS_PER_BLOCK + LAYERS_PER_BLOCK) % LAYERS_PER_BLOCK;
        int localZ = (z % ROWS_PER_BLOCK + ROWS_PER_BLOCK) % ROWS_PER_BLOCK;
        int blockX = (x - localX) / ROWS_PER_BLOCK;
        int blockY = (y - localY) / LAYERS_PER_BLOCK;
        int blockZ = (z - localZ) / ROWS_PER_BLOCK;
        BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);

        if (blockY >= 0)
        {
            Block block = world.getBlockState(blockPos).getBlock();
            if (block == BlockRegistry.billund)
            {
                TileEntity entity = world.getTileEntity(blockPos);
                if (entity != null && entity instanceof TileEntityBillund)
                {
                    TileEntityBillund billund = (TileEntityBillund) entity;
                    return billund.getStudLocal(localX, localY, localZ);
                }
            } else if (block != Blocks.AIR)
            {
                Colour colour = block.isVisuallyOpaque() ? billund.reference.Colour.TRANSLUCENT_WALL : billund.reference.Colour.WALL;
                return new Stud(colour, x, y, z, 1, 1, 1);
            }
        }
        return null;
    }

    public static boolean canSetStud(IBlockAccess world, int x, int y, int z)
    {
        int localX = (x % ROWS_PER_BLOCK + ROWS_PER_BLOCK) % ROWS_PER_BLOCK;
        int localY = (y % LAYERS_PER_BLOCK + LAYERS_PER_BLOCK) % LAYERS_PER_BLOCK;
        int localZ = (z % ROWS_PER_BLOCK + ROWS_PER_BLOCK) % ROWS_PER_BLOCK;
        int blockX = (x - localX) / ROWS_PER_BLOCK;
        int blockY = (y - localY) / LAYERS_PER_BLOCK;
        int blockZ = (z - localZ) / ROWS_PER_BLOCK;
        BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);

        if (blockY >= 0)
        {
            Block block = world.getBlockState(blockPos).getBlock();
            if (block == BlockRegistry.billund)
            {
                TileEntity entity = world.getTileEntity(blockPos);
                if (entity != null && entity instanceof TileEntityBillund)
                {
                    TileEntityBillund billund = (TileEntityBillund) entity;
                    return (billund.getStudLocal(localX, localY, localZ) == null);
                }
            } else if (block == Blocks.AIR)
                return true;
        }
        return false;
    }

    public static void setStud(World world, int x, int y, int z, Stud stud)
    {
        int localX = (x % ROWS_PER_BLOCK + ROWS_PER_BLOCK) % ROWS_PER_BLOCK;
        int localY = (y % LAYERS_PER_BLOCK + LAYERS_PER_BLOCK) % LAYERS_PER_BLOCK;
        int localZ = (z % ROWS_PER_BLOCK + ROWS_PER_BLOCK) % ROWS_PER_BLOCK;
        int blockX = (x - localX) / ROWS_PER_BLOCK;
        int blockY = (y - localY) / LAYERS_PER_BLOCK;
        int blockZ = (z - localZ) / ROWS_PER_BLOCK;
        BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);

        if (blockY >= 0)
        {
            Block block = world.getBlockState(blockPos).getBlock();
            if (block == BlockRegistry.billund)
            {
                // Add to existing billund block
                TileEntity entity = world.getTileEntity(blockPos);
                if (entity != null && entity instanceof TileEntityBillund)
                {
                    TileEntityBillund billund = (TileEntityBillund) entity;
                    billund.setStudLocal(localX, localY, localZ, stud);
                }
                world.markBlockRangeForRenderUpdate(blockPos, BlockPos.ORIGIN);
            } else if (block == Blocks.AIR)
            {
                // Add a new billund block
                if (world.setBlockState(blockPos, BlockRegistry.billund.getDefaultState()))
                {
                    TileEntity entity = world.getTileEntity(blockPos);
                    if (entity != null && entity instanceof TileEntityBillund)
                    {
                        TileEntityBillund billund = (TileEntityBillund) entity;
                        billund.setStudLocal(localX, localY, localZ, stud);
                    }
                    world.markBlockRangeForRenderUpdate(blockPos, BlockPos.ORIGIN);
                }
            }
        }
    }

    public static boolean canAddBrick(World world, Brick brick)
    {
        for (int x = brick.xOrigin; x < brick.xOrigin + brick.width; ++x)
            for (int y = brick.yOrigin; y < brick.yOrigin + brick.height; ++y)
                for (int z = brick.zOrigin; z < brick.zOrigin + brick.depth; ++z)
                    if (!canSetStud(world, x, y, z))
                        return false;
        return true;
    }

    public static void addBrick(World world, Brick brick)
    {
        for (int x = brick.xOrigin; x < brick.xOrigin + brick.width; ++x)
        {
            for (int y = brick.yOrigin; y < brick.yOrigin + brick.height; ++y)
            {
                for (int z = brick.zOrigin; z < brick.zOrigin + brick.depth; ++z)
                {
                    Stud stud = new Stud(brick, x - brick.xOrigin, y - brick.yOrigin, z - brick.zOrigin);
                    setStud(world, x, y, z, stud);
                }
            }
        }
    }

    public static void removeBrick(World world, Brick brick)
    {
        for (int x = brick.xOrigin; x < brick.xOrigin + brick.width; ++x)
            for (int y = brick.yOrigin; y < brick.yOrigin + brick.height; ++y)
                for (int z = brick.zOrigin; z < brick.zOrigin + brick.depth; ++z)
                    setStud(world, x, y, z, null);
    }

    public static class RaycastResult
    {
        public int hitX;
        public int hitY;
        public int hitZ;
        public int hitSide;
    }

    public static RaycastResult raycastStuds(World world, Vec3d origin, Vec3d direction, float distance)
    {
        float xScale = (float) ROWS_PER_BLOCK;
        float yScale = (float) LAYERS_PER_BLOCK;
        float zScale = (float) ROWS_PER_BLOCK;

        float x = (float) origin.xCoord * xScale;
        float y = (float) origin.yCoord * yScale;
        float z = (float) origin.zCoord * zScale;
        int sx = (int) Math.floor(x);
        int sy = (int) Math.floor(y);
        int sz = (int) Math.floor(z);
        x -= (float) sx;
        y -= (float) sy;
        z -= (float) sz;

        float dx = (float) direction.xCoord;
        float dy = (float) direction.yCoord * (yScale / xScale);
        float dz = (float) direction.zCoord;
        {
            float dLen = (float) Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
            dx /= dLen;
            dy /= dLen;
            dz /= dLen;
            distance *= xScale;
        }

        int stepX = 0;
        int stepY = 0;
        int stepZ = 0;
        float edgeX = 0.0f;
        float edgeY = 0.0f;
        float edgeZ = 0.0f;
        if (dx > 0.0f)
        {
            stepX = 1;
            edgeX = 1.0f;
        } else if (dx < 0.0f)
        {
            stepX = -1;
            edgeX = 0.0f;
        }
        if (dy > 0.0f)
        {
            stepY = 1;
            edgeY = 1.0f;
        } else if (dy < 0.0f)
        {
            stepY = -1;
            edgeY = 0.0f;
        }
        if (dz > 0.0f)
        {
            stepZ = 1;
            edgeZ = 1.0f;
        } else if (dz < 0.0f)
        {
            stepZ = -1;
            edgeZ = 0.0f;
        }

        float distanceLeft = distance;
        Stud hitStud = null;
        int hitSide = -1;
        while (distanceLeft > 0.0f && hitStud == null)
        {
            float distToEdgeX = 999.0f;
            float distToEdgeY = 999.0f;
            float distToEdgeZ = 999.0f;
            if (stepX != 0)
                distToEdgeX = (edgeX - x) / dx;
            if (stepY != 0)
                distToEdgeY = (edgeY - y) / dy;
            if (stepZ != 0)
                distToEdgeZ = (edgeZ - z) / dz;
            if (distToEdgeX <= distToEdgeY && distToEdgeX <= distToEdgeZ)
            {
                if (distToEdgeX < distanceLeft)
                {
                    sx += stepX;
                    x += (distToEdgeX * dx) - (float) stepX;
                    y += distToEdgeX * dy;
                    z += distToEdgeX * dz;
                    distanceLeft -= distToEdgeX;

                    hitStud = getStud(world, sx, sy, sz);
                    hitSide = (stepX > 0) ? 4 : 5;
                } else
                {
                    x += distToEdgeX * dx;
                    y += distToEdgeX * dy;
                    z += distToEdgeX * dz;
                    distanceLeft = 0.0f;
                }
            } else if (distToEdgeY <= distToEdgeX && distToEdgeY <= distToEdgeZ)
            {
                if (distToEdgeY < distanceLeft)
                {
                    sy += stepY;
                    x += distToEdgeY * dx;
                    y += (distToEdgeY * dy) - (float) stepY;
                    z += distToEdgeY * dz;
                    distanceLeft -= distToEdgeY;

                    hitStud = getStud(world, sx, sy, sz);
                    hitSide = (stepY > 0) ? 0 : 1;
                } else
                {
                    x += distToEdgeY * dx;
                    y += distToEdgeY * dy;
                    z += distToEdgeY * dz;
                    distanceLeft = 0.0f;
                }
            } else
            {
                if (distToEdgeZ < distanceLeft)
                {
                    sz += stepZ;
                    x += distToEdgeZ * dx;
                    y += distToEdgeZ * dy;
                    z += (distToEdgeZ * dz) - (float) stepZ;
                    distanceLeft -= distToEdgeZ;

                    hitStud = getStud(world, sx, sy, sz);
                    hitSide = (stepZ > 0) ? 2 : 3;
                } else
                {
                    x += distToEdgeZ * dx;
                    y += distToEdgeZ * dy;
                    z += distToEdgeZ * dz;
                    distanceLeft = 0.0f;
                }
            }
        }

        if (hitStud != null)
        {
            RaycastResult result = new RaycastResult();
            result.hitX = sx;
            result.hitY = sy;
            result.hitZ = sz;
            result.hitSide = hitSide;
            return result;
        }
        return null;
    }
}
