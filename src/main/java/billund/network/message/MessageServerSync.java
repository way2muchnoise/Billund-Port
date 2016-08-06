package billund.network.message;

import billund.registry.BillundSetRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageServerSync implements IMessage, IMessageHandler<MessageServerSync, IMessage>
{
    String message;

    public MessageServerSync()
    {
        this.message = BillundSetRegistry.instance().asString();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        int lenght = buf.readInt();
        this.message = new String(buf.readBytes(lenght).array());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.message.getBytes().length);
        buf.writeBytes(this.message.getBytes());
    }

    @Override
    public IMessage onMessage(MessageServerSync message, MessageContext ctx)
    {
        BillundSetRegistry.instance().serverSync(message.message);
        return null;
    }
}
