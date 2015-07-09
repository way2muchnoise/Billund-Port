/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.network.message;

import billund.tileentity.TileEntityAirDrop;
import billund.util.BillundSet;
import billund.util.EmeraldHelper;
import billund.util.LogHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Random;

public class MessageBillundOrder implements IMessage
{
    public int[] sets;

    public MessageBillundOrder()
    {
        this.sets = new int[0];
    }

    public MessageBillundOrder(int[] sets)
    {
        this.sets = sets;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int amount = buf.readInt();
        sets = new int[amount];
        for (int i = 0; i < amount; i++)
            sets[i] = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(sets.length);
        if (sets != null)
            for (int i : sets)
                buf.writeInt(i);
    }

    public static class Handler implements IMessageHandler<MessageBillundOrder, IMessage>
    {

        @Override
        public IMessage onMessage(MessageBillundOrder message, MessageContext ctx)
        {
            String sets = "";
            for (int set : message.sets)
                sets += BillundSet.get(set).getDescription() + ", ";
            LogHelper.info(String.format("Received order for %s from %s", sets.substring(0, sets.length()-2), ctx.getServerHandler().playerEntity.getDisplayName()));
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            World world = player.worldObj;
            Random r = new Random();
            for (int set : message.sets)
            {
                int cost = BillundSet.get(set).getCost();
                if (EmeraldHelper.removeEmeralds(player, cost))
                {
                    world.spawnEntityInWorld(new TileEntityAirDrop(
                            world,
                            Math.floor(player.posX - 8 + r.nextInt(16)) + 0.5f,
                            Math.min(world.getHeight(), 255) - r.nextInt(32) - 0.5f,
                            Math.floor(player.posZ - 8 + r.nextInt(16)) + 0.5f,
                            set
                    ));
                }
            }
            return null;
        }
    }
}
