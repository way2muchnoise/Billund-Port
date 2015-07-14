/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.network.message;

import billund.registry.BillundSetRegistry;
import billund.set.BillundSet;
import billund.tileentity.TileEntityAirDrop;
import billund.util.EmeraldHelper;
import billund.util.LogHelper;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Random;

public class MessageBillundOrder implements IMessage, IMessageHandler<MessageBillundOrder, IMessage>
{
    public String[] sets;

    public MessageBillundOrder()
    {
        this.sets = new String[0];
    }

    public MessageBillundOrder(String[] sets)
    {
        this.sets = sets;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int amount = buf.readInt();
        sets = new String[amount];
        for (int i = 0; i < amount; i++)
        {
            int length = buf.readInt();
            sets[i] = new String(buf.readBytes(length).array());
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(sets.length);
        if (sets != null)
        {
            for (String set : sets)
            {
                byte[] bytes = set.getBytes();
                buf.writeInt(bytes.length);
                buf.writeBytes(bytes);
            }
        }
    }

    @Override
    public IMessage onMessage(MessageBillundOrder message, MessageContext ctx)
    {
        String sets = "";
        for (String set : message.sets)
            sets += set + ", ";
        LogHelper.info(String.format("Received order for %s from %s", sets.substring(0, sets.length()-2), ctx.getServerHandler().playerEntity.getDisplayName()));
        EntityPlayer player = ctx.getServerHandler().playerEntity;
        World world = player.worldObj;
        Random r = new Random();
        for (String sSet : message.sets)
        {
            BillundSet set = BillundSetRegistry.instance().getBillundSet(sSet);
            int cost = set.getCost();
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
