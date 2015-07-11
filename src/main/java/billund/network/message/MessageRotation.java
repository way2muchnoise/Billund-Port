package billund.network.message;

import billund.registry.RotationRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageRotation implements IMessage, IMessageHandler<MessageRotation, IMessage>
{
    private byte rotation;

    public MessageRotation()
    {
        this.rotation = 0;
    }

    public MessageRotation(byte rotation)
    {
        this.rotation = rotation;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.rotation = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeByte(this.rotation);
    }

    @Override
    public IMessage onMessage(MessageRotation message, MessageContext ctx)
    {
        RotationRegistry.instance().setRotation(ctx.getServerHandler().playerEntity, message.rotation);
        return null;
    }
}
