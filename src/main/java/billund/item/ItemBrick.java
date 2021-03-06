/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.item;

import billund.proxy.ClientProxy;
import billund.reference.Colour;
import billund.reference.Names;
import billund.registry.ItemRegistry;
import billund.registry.RotationRegistry;
import billund.set.Bricks;
import billund.util.Brick;
import billund.util.Stud;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;


public class ItemBrick extends ItemBillund
{
    public ItemBrick()
    {
        super();
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setUnlocalizedName(Names.Items.BRICK);
    }

    public static ItemStack create(Colour colour, int width, int depth, int quantity)
    {
        int damage = ((width - 1) & 0x1) + (((depth - 1) & 0x7) << 1) + ((colour.number & 0xf) << 4);
        return new ItemStack(ItemRegistry.brick, quantity, damage);
    }

    public static ItemStack create(Bricks brick)
    {
        return create(brick.colour, brick.width, brick.depth, brick.quantity);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        for (Colour colour : Colour.values())
        {
            list.add(create(colour, 1, 1, 1));
            list.add(create(colour, 1, 2, 1));
            list.add(create(colour, 1, 3, 1));
            list.add(create(colour, 1, 4, 1));
            list.add(create(colour, 1, 6, 1));
            list.add(create(colour, 2, 2, 1));
            list.add(create(colour, 2, 3, 1));
            list.add(create(colour, 2, 4, 1));
            list.add(create(colour, 2, 6, 1));
        }
    }

    public static Stud.RaycastResult raycastFromPlayer(World world, EntityPlayer player, float f)
    {
        // Calculate the raycast origin and direction
        double yOffset2 = (!world.isRemote && player.isSneaking()) ? -0.08 : 0.0; // TODO: Improve
        float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
        float yaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
        double x = player.prevPosX + (player.posX - player.prevPosX) * (double) f;
        double y = player.prevPosY + (player.posY - player.prevPosY) * (double) f + 1.62 - player.getYOffset() + yOffset2; // TODO: Improve
        double z = player.prevPosZ + (player.posZ - player.prevPosZ) * (double) f;
        Vec3d position = new Vec3d(x, y, z);

        float f3 = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
        float f4 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
        float f5 = -MathHelper.cos(-pitch * 0.017453292F);
        float f6 = MathHelper.sin(-pitch * 0.017453292F);
        float f7 = f4 * f5;
        float f8 = f3 * f5;

        float distance = 5.0f;
        if (player instanceof EntityPlayerMP)
        {
            distance = (float) ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
        }
        Vec3d direction = new Vec3d( f7, f6, f8);

        // Do the raycast
        return Stud.raycastStuds(world, position, direction, distance);
    }

    public static Brick getPotentialBrick(ItemStack stack, World world, EntityPlayer player, float f)
    {
        // Do the raycast
        Stud.RaycastResult result = raycastFromPlayer(world, player, f);
        byte rotation = world.isRemote ? ClientProxy.rotation : RotationRegistry.instance().getRotation(player);
        if (result != null)
        {
            // Calculate where to place the brick
            int width = getWidth(stack);
            int depth = getDepth(stack);
            int height = 1;
            if (rotation % 2 != 0)
            {
                int temp = depth;
                depth = width;
                width = temp;
            }

            int placeX = result.hitX - (rotation == 3 ? width - 1 : 0);
            int placeY = result.hitY;
            int placeZ = result.hitZ - (rotation == 2 ? depth - 1 : 0);
            switch (result.hitSide)
            {
                case 0:
                    placeY -= height;
                    break;
                case 1:
                    placeY++;
                    break;
                case 2:
                    placeZ -= depth;
                    break;
                case 3:
                    placeZ++;
                    break;
                case 4:
                    placeX -= width;
                    break;
                case 5:
                    placeX++;
                    break;
            }

            // Try a few positions nearby
            Brick brick = new Brick(getColour(stack), placeX, placeY, placeZ, width, height, depth);
            for (int x = 0; x < width; ++x)
            {
                for (int z = 0; z < depth; ++z)
                {
                    for (int y = 0; y < height; ++y)
                    {
                        brick.xOrigin = placeX - x;
                        brick.yOrigin = placeY - y;
                        brick.zOrigin = placeZ - z;
                        if (Stud.canAddBrick(world, brick))
                            return brick;
                    }
                }
            }
        }
        return null;
    }

    public static Brick getExistingBrick(World world, EntityPlayer player, float f)
    {
        // Do the raycast
        Stud.RaycastResult result = raycastFromPlayer(world, player, f);
        if (result != null)
        {
            Stud stud = Stud.getStud(world, result.hitX, result.hitY, result.hitZ);
            if (stud != null && stud.Colour != Colour.WALL)
            {
                return new Brick(stud.Colour, stud.XOrigin, stud.YOrigin, stud.ZOrigin, stud.BrickWidth, stud.BrickHeight, stud.BrickDepth);
            }
        }
        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer player, EnumHand hand)
    {
        Brick brick = getPotentialBrick(stack, world, player, 1.0f);
        if (brick != null)
        {
            if (!world.isRemote)
            {
                // Place the brick
                Stud.addBrick(world, brick);

                if (!player.capabilities.isCreativeMode)
                {
                    // Decrement stackSize
                    stack.stackSize--;
                    return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
                }
            }
        }
        return ActionResult.newResult(EnumActionResult.PASS, stack);
    }

    public static int getWidth(ItemStack stack)
    {
        int damage = stack.getItemDamage();
        return (damage & 0x1) + 1;
    }

    public static int getHeight(ItemStack stack)
    {
        return 1;
    }

    public static int getDepth(ItemStack stack)
    {
        int damage = stack.getItemDamage();
        return ((damage >> 1) & 0x7) + 1;
    }

    public static Colour getColour(ItemStack stack)
    {
        int damage = stack.getItemDamage();
        return Colour.values()[((damage >> 4) & 0xf)];
    }
}
