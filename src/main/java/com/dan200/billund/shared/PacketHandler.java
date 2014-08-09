/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.dan200.billund.shared;

import com.dan200.billund.Billund;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class PacketHandler implements IMessageHandler
{
	public void onPacketData(NetworkManager manager, C17PacketCustomPayload _packet, EntityPlayer player)
	{
		try
		{
			BillundPacket packet = BillundPacket.parse( _packet.func_149558_e() );
			Billund.handlePacket( packet, player );
		}
		catch( Exception e )
		{
			// Something failed, ignore it
			//e.printStackTrace();
		}
	}

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(BuildInfo.ModName.toLowerCase());

    public static void init() {
        INSTANCE.registerMessage(BillundPacket.Handler.class, BillundPacket.class, 0, Side.CLIENT);
    }

    @Override
    public IMessage onMessage(IMessage message, MessageContext ctx) {
        return null;
    }
}
