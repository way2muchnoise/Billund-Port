package billund.handler;

import billund.block.BlockBillund;
import billund.render.BrickRenderHelper;
import billund.item.ItemBrick;
import billund.util.Brick;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

public class ForgeEventHandler
{
    // Forge event responses

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event)
    {
        // Draw the preview brick
        float f = event.partialTicks;
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World world = Minecraft.getMinecraft().theWorld;
        if (player != null && world != null)
        {
            ItemStack currentStack = player.inventory.getCurrentItem();
            if (currentStack != null && currentStack.getItem() instanceof ItemBrick)
            {
                Brick brick = ItemBrick.getPotentialBrick(currentStack, player.worldObj, player, f);
                if (brick != null)
                {
                    // Setup
                    GL11.glPushMatrix();
                    GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ZERO);
                    GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    GL11.glDisable(GL11.GL_LIGHTING);

                    translateToWorldCoords(Minecraft.getMinecraft().renderViewEntity, f);
                    BrickRenderHelper.renderBrick(world, brick);

                    // Teardown
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    GL11.glEnable(GL11.GL_LIGHTING);
                    GL11.glPopMatrix();
                }
            }
        }
    }

    private void translateToWorldCoords(Entity entity, float frame)
    {
        double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * frame;
        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * frame;
        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * frame;
        GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);
    }

    // Tick handler

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        World world = Minecraft.getMinecraft().theWorld;
        // See what brick is in front of the player
        float f = 1F; //TODO:((Float)tickData).getFloatValue(); was this dunno what it did yet :p

        if (player != null && world != null)
            BlockBillund.setHoverBrick(ItemBrick.getExistingBrick(world, player, f));
        else
            BlockBillund.setHoverBrick(null);
    }
}
