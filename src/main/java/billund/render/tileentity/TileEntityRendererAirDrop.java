/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package billund.render.tileentity;

import billund.render.models.ModelParachute;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class TileEntityRendererAirDrop extends Render
{
    private ModelParachute m_model;

    public TileEntityRendererAirDrop()
    {
        super(null);
        this.shadowSize = 0.5f;
        m_model = new ModelParachute();
    }

    /*private void doRenderFallingSand(TileEntityAirDrop entity, double x, double y, double z, float f, float f2)
    {
        World world = entity.getWorld();
        Block block = entity.block;

        if (world.getBlock(MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ)) != entity.block)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x - 0.5f, (float) y - 0.5f, (float) z - 0.5f);
            this.bindEntityTexture(entity);

            GL11.glDisable(GL11.GL_LIGHTING);

            if (block != null)
            {
                TileEntityRendererChestHelper.instance.renderChest(block, 0, 0.0f);

                if (!entity.deployed)
                {
                    GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f);
                    GL11.glTranslatef(-0.5f, -0.5f, 0.5f);
                    GL11.glScalef(1.2f, 1.2f, 1.2f);

                    Minecraft mc = Minecraft.getMinecraft();
                    mc.getTextureManager().bindTexture(Resources.Model.PARACHUTE);
                    m_model.render(0.0625f);
                }
            }

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glPopMatrix();
        }
    }*/

    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

    public void doRender(Entity entity, double x, double y, double z, float f, float f2)
    {
        //this.doRenderFallingSand((TileEntityAirDrop) entity, x, y, z, f, f2);
    }
}
