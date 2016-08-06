/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.network;

import billund.network.message.MessageBillundOrder;
import billund.network.message.MessageRotation;
import billund.network.message.MessageServerSync;
import billund.network.message.MessageTileEntityBillund;
import billund.reference.Reference;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class MessageHandler implements IMessageHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.NAME.toLowerCase());
    private static int ID = 0;

    public static void init()
    {
        INSTANCE.registerMessage(MessageBillundOrder.class, MessageBillundOrder.class, ID++, Side.SERVER);
        INSTANCE.registerMessage(MessageRotation.class, MessageRotation.class, ID++, Side.SERVER);
        INSTANCE.registerMessage(MessageTileEntityBillund.class, MessageTileEntityBillund.class, ID++, Side.CLIENT);
        INSTANCE.registerMessage(MessageServerSync.class, MessageServerSync.class, ID++, Side.CLIENT);
    }

    @Override
    public IMessage onMessage(IMessage message, MessageContext ctx)
    {
        return null;
    }
}
