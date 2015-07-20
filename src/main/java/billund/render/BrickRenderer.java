package billund.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class BrickRenderer implements IItemRenderer
{
    // IItemRenderer implementation
    @Override
    public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
    {
        switch (type)
        {
            case ENTITY:
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
            case INVENTORY:
            {
                return true;
            }
            case FIRST_PERSON_MAP:
            default:
            {
                return false;
            }
        }
    }

    @Override
    public boolean shouldUseRenderHelper(IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper)
    {
        switch (helper)
        {
            case ENTITY_ROTATION:
            case ENTITY_BOBBING:
            case EQUIPPED_BLOCK:
            case BLOCK_3D:
            case INVENTORY_BLOCK:
                return true;
            default:
                return false;
        }
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object[] data)
    {
        switch (type)
        {
            case ENTITY:
            {
                BrickRenderHelper.renderBrick(item, false, true);
                break;
            }
            case EQUIPPED:
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(0.6f, 0.6f, 0.6f);
                BrickRenderHelper.renderBrick(item, false, true);
                GL11.glPopMatrix();
                break;
            }
            case INVENTORY:
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(0.5f, 0.5f, 0.5f);
                BrickRenderHelper.renderBrick(item, true, true);
                GL11.glPopMatrix();
                break;
            }
            case EQUIPPED_FIRST_PERSON:
            default:
                break;
        }
    }
}
