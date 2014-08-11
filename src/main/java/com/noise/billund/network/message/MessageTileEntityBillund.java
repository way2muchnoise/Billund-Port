package com.noise.billund.network.message;

import com.noise.billund.tileentity.TileEntityBillund;
import com.noise.billund.util.BillundSet;
import com.noise.billund.util.LogHelper;
import com.noise.billund.util.Stud;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

public class MessageTileEntityBillund implements IMessage {

    public Stud[] studs;
    public int x, y, z;

    public MessageTileEntityBillund() {
        this.studs = new Stud[Stud.STUDS_PER_BLOCK];
    }

    public MessageTileEntityBillund(int x, int y, int z, Stud[] studs) {
        this.studs = studs;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        if(buf.isReadable()) {
            int j = buf.readInt();
            for (int i = 0; i < Stud.STUDS_PER_BLOCK; ++i) {
                if (j == i) {
                    Stud stud = new Stud();
                    stud.Colour = buf.readInt();
                    stud.XOrigin = buf.readInt();
                    stud.YOrigin = buf.readInt();
                    stud.ZOrigin = buf.readInt();
                    stud.BrickWidth = buf.readInt();
                    stud.BrickHeight = buf.readInt();
                    stud.BrickDepth = buf.readInt();
                    this.studs[i] = stud;
                    if (buf.isReadable()) {
                        j = buf.readInt();
                    }
                } else {
                    this.studs[i] = null;
                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        for( int i=0; i<Stud.STUDS_PER_BLOCK; ++i )
        {
            Stud stud = this.studs[i];
            if( stud != null ) {
                buf.writeInt(i);
                buf.writeInt(stud.Colour);
                buf.writeInt(stud.XOrigin);
                buf.writeInt(stud.YOrigin);
                buf.writeInt(stud.ZOrigin);
                buf.writeInt(stud.BrickWidth);
                buf.writeInt(stud.BrickHeight);
                buf.writeInt(stud.BrickDepth);
            }
        }
    }

    public static class Handler implements IMessageHandler<MessageTileEntityBillund, IMessage> {

        @Override
        public IMessage onMessage(MessageTileEntityBillund message, MessageContext ctx) {
            TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.x, message.y, message.z);

            if (tileEntity instanceof TileEntityBillund)
            {
                ((TileEntityBillund) tileEntity).setStuds(message.studs);
            }
            FMLClientHandler.instance().getClient().theWorld.markBlockForUpdate(message.x, message.y, message.z);
            LogHelper.info(String.format("Received TE with lenght %s from", BillundSet.get(message.studs.length)));
            return null;
        }
    }
}
