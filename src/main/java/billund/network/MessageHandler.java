/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.network;

import billund.network.message.MessageBillund;
import billund.network.message.MessageTileEntityBillund;
import billund.reference.Reference;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class MessageHandler implements IMessageHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.MOD_NAME.toLowerCase());

    public static void init()
    {
        INSTANCE.registerMessage(MessageBillund.Handler.class, MessageBillund.class, 0, Side.SERVER);
        INSTANCE.registerMessage(MessageTileEntityBillund.Handler.class, MessageTileEntityBillund.class, 1, Side.CLIENT);
    }

    @Override
    public IMessage onMessage(IMessage message, MessageContext ctx)
    {
        return null;
    }
}
