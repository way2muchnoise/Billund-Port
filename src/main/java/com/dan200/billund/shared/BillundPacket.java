/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.dan200.billund.shared;

import com.dan200.billund.Billund;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BillundPacket implements IMessage
{
	// Static Packet types
	public static final byte OrderSet = 1;
	
	// Packet class
	public byte packetType;
	public String[] dataString;
	public int[] dataInt;
	
	public BillundPacket()
	{
		packetType = 0;
		dataString = null;
		dataInt = null;
	}

	private void writeData( DataOutputStream data ) throws IOException
	{
		data.writeByte(packetType);
		if( dataString != null ) {
			data.writeByte(dataString.length);
		} else {
			data.writeByte(0);
		}
		if( dataInt != null ) {
			data.writeByte(dataInt.length);
		} else {
			data.writeByte(0);
		}
		if( dataString != null ) {
			for(String s : dataString) {
				data.writeUTF(s);
			}
		}
		if( dataInt != null ) {
			for(int i : dataInt) {
				data.writeInt(i);
			}
		}
	}

	private void readData( DataInputStream data ) throws IOException
	{
		packetType = data.readByte();
		byte nString = data.readByte();
		byte nInt = data.readByte();
		if(nString > 128 || nInt > 128 || nString < 0 || nInt < 0 ) {
			throw new IOException("");
		}
		if(nString == 0) {
			dataString = null;
 		} else {
			dataString = new String[nString];
			for(int k = 0; k < nString; k++) {
				dataString[k] = data.readUTF();
			}
		}
		if(nInt == 0) {
			dataInt = null;
		} else {
			dataInt = new int[nInt];
			for(int k = 0; k < nInt; k++) {
				dataInt[k] = data.readInt();
			}
		}
	}

    @Override
    public void fromBytes(ByteBuf buf) {
        try {
            readData(new DataInputStream(new ByteBufInputStream(buf)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        try {
            writeData(new DataOutputStream(new ByteBufOutputStream(buf)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Handler implements IMessageHandler<BillundPacket, IMessage> {

        @Override
        public IMessage onMessage(BillundPacket message, MessageContext ctx) {
            System.out.println(String.format("Received %s from %s", message.dataInt[0], ctx.getServerHandler().playerEntity.getDisplayName()));
            Billund.handlePacket(message, ctx.getServerHandler().playerEntity);
            return null;
        }
    }
}
