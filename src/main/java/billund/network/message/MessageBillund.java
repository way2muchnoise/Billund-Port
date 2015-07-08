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
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class MessageBillund implements IMessage
{
    // Static Packet types
    public static final byte OrderSet = 1;

    // Packet class
    public byte packetType;
    public String[] dataString;
    public int[] dataInt;

    public MessageBillund()
    {
        packetType = 0;
        dataString = null;
        dataInt = null;
    }

    private void writeData(DataOutputStream data) throws IOException
    {
        data.writeByte(packetType);
        if (dataString != null)
        {
            data.writeByte(dataString.length);
        } else
        {
            data.writeByte(0);
        }
        if (dataInt != null)
        {
            data.writeByte(dataInt.length);
        } else
        {
            data.writeByte(0);
        }
        if (dataString != null)
        {
            for (String s : dataString)
            {
                data.writeUTF(s);
            }
        }
        if (dataInt != null)
        {
            for (int i : dataInt)
            {
                data.writeInt(i);
            }
        }
    }

    private void readData(DataInputStream data) throws IOException
    {
        packetType = data.readByte();
        byte nString = data.readByte();
        byte nInt = data.readByte();
        if (nString > 128 || nInt > 128 || nString < 0 || nInt < 0)
        {
            throw new IOException("");
        }
        if (nString == 0)
        {
            dataString = null;
        } else
        {
            dataString = new String[nString];
            for (int k = 0; k < nString; k++)
            {
                dataString[k] = data.readUTF();
            }
        }
        if (nInt == 0)
        {
            dataInt = null;
        } else
        {
            dataInt = new int[nInt];
            for (int k = 0; k < nInt; k++)
            {
                dataInt[k] = data.readInt();
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        try
        {
            readData(new DataInputStream(new ByteBufInputStream(buf)));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        try
        {
            writeData(new DataOutputStream(new ByteBufOutputStream(buf)));
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static class Handler implements IMessageHandler<MessageBillund, IMessage>
    {

        @Override
        public IMessage onMessage(MessageBillund message, MessageContext ctx)
        {
            LogHelper.info(String.format("Received order for %s from %s", BillundSet.get(message.dataInt[0]).getDescription(), ctx.getServerHandler().playerEntity.getDisplayName()));
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            switch (message.packetType)
            {
                case MessageBillund.OrderSet:
                {
                    World world = player.worldObj;
                    int set = message.dataInt[0];
                    int cost = BillundSet.get(set).getCost();
                    if (EmeraldHelper.removeEmeralds(player, cost))
                    {
                        Random r = new Random();
                        world.spawnEntityInWorld(new TileEntityAirDrop(
                                world,
                                Math.floor(player.posX - 8 + r.nextInt(16)) + 0.5f,
                                Math.min(world.getHeight(), 255) - r.nextInt(32) - 0.5f,
                                Math.floor(player.posZ - 8 + r.nextInt(16)) + 0.5f,
                                set
                        ));
                    }
                    break;
                }
            }
            return null;
        }
    }
}
