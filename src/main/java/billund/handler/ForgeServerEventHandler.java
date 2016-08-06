package billund.handler;

import billund.network.MessageHandler;
import billund.network.message.MessageServerSync;
import billund.registry.ItemRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Random;

// Forge server event responses
public class ForgeServerEventHandler
{
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
        if (event.getEntity() instanceof EntityPlayerMP)
            MessageHandler.INSTANCE.sendTo(new MessageServerSync(), (EntityPlayerMP) event.getEntity());
    }

    private Random r = new Random();

    @SubscribeEvent
    public void onEntityLivingDeath(LivingDeathEvent event)
    {
        if (event.getEntity().worldObj.isRemote) return;

        if (event.getEntity() instanceof EntityZombie)
        {
            EntityLivingBase living = (EntityLivingBase) event.getEntity();
            if ((living.isChild() && r.nextInt(20) == 0) || (!living.isChild() && r.nextInt(100) == 0))
                event.getEntity().entityDropItem(new ItemStack(ItemRegistry.orderForm, 1), 0.0f);
        }
    }
}
