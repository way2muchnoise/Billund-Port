package billund.handler;

import billund.network.MessageHandler;
import billund.network.message.MessageServerSync;
import billund.registry.ItemRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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
        if (event.entity instanceof EntityPlayerMP)
            MessageHandler.INSTANCE.sendTo(new MessageServerSync(), (EntityPlayerMP)event.entity);
    }

    private Random r = new Random();

    @SubscribeEvent
    public void onEntityLivingDeath(LivingDeathEvent event)
    {
        if (event.entity.worldObj.isRemote) return;

        if (event.entity instanceof EntityZombie)
        {
            EntityLivingBase living = (EntityLivingBase) event.entity;
            if ((living.isChild() && r.nextInt(20) == 0) || (!living.isChild() && r.nextInt(100) == 0))
                event.entity.entityDropItem(new ItemStack(ItemRegistry.orderForm, 1), 0.0f);
        }
    }
}
