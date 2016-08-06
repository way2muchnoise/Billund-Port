package billund.network.message;

import billund.reference.Colour;
import billund.tileentity.TileEntityBillund;
import billund.util.LogHelper;
import billund.util.Stud;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

public class MessageTileEntityBillund implements IMessage, IMessageHandler<MessageTileEntityBillund, IMessage>
{

    public Stud[] studs;
    public BlockPos pos;

    public MessageTileEntityBillund()
    {
        this.studs = new Stud[Stud.STUDS_PER_BLOCK];
    }

    public MessageTileEntityBillund(BlockPos pos, Stud[] studs)
    {
        this.studs = studs;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = BlockPos.fromLong(buf.readLong());
        if (buf.isReadable())
        {
            int j = buf.readInt();
            for (int i = 0; i < Stud.STUDS_PER_BLOCK; ++i)
            {
                if (j == i)
                {
                    Stud stud = new Stud();
                    stud.Colour = Colour.getValue(buf.readInt());
                    stud.XOrigin = buf.readInt();
                    stud.YOrigin = buf.readInt();
                    stud.ZOrigin = buf.readInt();
                    stud.BrickWidth = buf.readInt();
                    stud.BrickHeight = buf.readInt();
                    stud.BrickDepth = buf.readInt();
                    this.studs[i] = stud;
                    if (buf.isReadable())
                    {
                        j = buf.readInt();
                    }
                } else
                {
                    this.studs[i] = null;
                }
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        for (int i = 0; i < Stud.STUDS_PER_BLOCK; ++i)
        {
            Stud stud = this.studs[i];
            if (stud != null)
            {
                buf.writeInt(i);
                buf.writeInt(stud.Colour.number);
                buf.writeInt(stud.XOrigin);
                buf.writeInt(stud.YOrigin);
                buf.writeInt(stud.ZOrigin);
                buf.writeInt(stud.BrickWidth);
                buf.writeInt(stud.BrickHeight);
                buf.writeInt(stud.BrickDepth);
            }
        }
    }

    @Override
    public IMessage onMessage(MessageTileEntityBillund message, MessageContext ctx)
    {
        TileEntity tileEntity = FMLClientHandler.instance().getClient().theWorld.getTileEntity(message.pos);

        if (tileEntity instanceof TileEntityBillund)
        {
            ((TileEntityBillund) tileEntity).setStuds(message.studs);
            FMLClientHandler.instance().getClient().theWorld.markBlockRangeForRenderUpdate(message.pos, BlockPos.ORIGIN);
            LogHelper.debug(String.format("Brick at x:%s y:%s z:%s", message.pos.getX(), message.pos.getY(), message.pos.getZ()));
        }
        return null;
    }
}
