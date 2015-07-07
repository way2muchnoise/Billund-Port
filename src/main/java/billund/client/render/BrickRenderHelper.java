package billund.client.render;

import billund.block.BlockBillund;
import billund.init.ModBlocks;
import billund.item.ItemBrick;
import billund.reference.Colour;
import billund.util.Brick;
import billund.util.Stud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class BrickRenderHelper
{
    public static void renderBrick(ItemStack brick, boolean scale, boolean center) {
        Tessellator tessellator = Tessellator.instance;
        int brightness = 15;

        int colour = ItemBrick.getColour(brick).number;
        int width = ItemBrick.getWidth(brick);
        int height = ItemBrick.getHeight(brick);
        int depth = ItemBrick.getDepth(brick);

        // Setup
        GL11.glPushMatrix();

        if (scale) {
            float scaleValue = ((float) Stud.LAYERS_PER_BLOCK) / Math.max(2.0f, (float) Math.max(width, depth) - 0.5f);
            GL11.glScalef(scaleValue, scaleValue, scaleValue);
        }
        if (center) {
            GL11.glTranslatef(
                    -0.5f * ((float) width / (float) Stud.ROWS_PER_BLOCK),
                    -0.5f * ((float) height / (float) Stud.LAYERS_PER_BLOCK),
                    -0.5f * ((float) depth / (float) Stud.ROWS_PER_BLOCK)
            );
        }

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(mc.getTextureManager().getResourceLocation(0)); // bind the terrain texture

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        renderBrick(null, brightness, colour, 0, 0, 0, width, height, depth);
        tessellator.draw();

        // Teardown
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public static void renderBrick(IBlockAccess world, Brick brick) {
        int localX = (brick.XOrigin % Stud.ROWS_PER_BLOCK + Stud.ROWS_PER_BLOCK) % Stud.ROWS_PER_BLOCK;
        int localY = (brick.YOrigin % Stud.LAYERS_PER_BLOCK + Stud.LAYERS_PER_BLOCK) % Stud.LAYERS_PER_BLOCK;
        int localZ = (brick.ZOrigin % Stud.ROWS_PER_BLOCK + Stud.ROWS_PER_BLOCK) % Stud.ROWS_PER_BLOCK;
        int blockX = (brick.XOrigin - localX) / Stud.ROWS_PER_BLOCK;
        int blockY = (brick.YOrigin - localY) / Stud.LAYERS_PER_BLOCK;
        int blockZ = (brick.ZOrigin - localZ) / Stud.ROWS_PER_BLOCK;

        Tessellator tessellator = Tessellator.instance;
        int brightness = ModBlocks.billund.getMixedBrightnessForBlock(world, blockX, blockY, blockZ);

        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(mc.getTextureManager().getResourceLocation(0)); // bind the terrain texture

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0f, -1.0f, 0.0f);
        renderBrick(world, brightness, brick.Colour.number, brick.XOrigin, brick.YOrigin, brick.ZOrigin, brick.Width, brick.Height, brick.Depth);
        tessellator.draw();
    }

    public static void renderBrick(IBlockAccess world, int brightness, int colour, int sx, int sy, int sz, int width, int height, int depth) {
        // Draw the brick
        IIcon icon = BlockBillund.getIcon(colour);
        if (world != null) {
            Tessellator tessellator = Tessellator.instance;
            tessellator.setBrightness(brightness);
        }

        float pixel = 1.0f / 96.0f;
        float xBlockSize = (float) Stud.STUDS_PER_ROW;
        float yBlockSize = (float) Stud.STUDS_PER_COLUMN;
        float zBlockSize = (float) Stud.STUDS_PER_ROW;

        float startX = (float) sx / xBlockSize;
        float startY = (float) sy / yBlockSize;
        float startZ = (float) sz / zBlockSize;
        float endX = startX + ((float) width / xBlockSize);
        float endY = startY + ((float) height / yBlockSize);
        float endZ = startZ + ((float) depth / zBlockSize);
        renderBox(
                icon, brightness,
                startX, startY, startZ,
                endX, endY, endZ,
                true
        );

        // Draw the studs
        int sny = sy + height;
        startY = (float) sny / yBlockSize;
        endY = startY + (0.1666f / yBlockSize);
        for (int snx = sx; snx < sx + width; ++snx) {
            startX = (float) snx / xBlockSize;
            endX = startX + (1.0f / xBlockSize);
            for (int snz = sz; snz < sz + depth; ++snz) {
                boolean drawStud;
                if (world != null) {
                    Stud above = Stud.getStud(world, snx, sny, snz);
                    drawStud = (above == null) || (above.Colour == Colour.TRANSLUCENT_WALL);
                } else {
                    drawStud = true;
                }

                if (drawStud) {
                    startZ = (float) snz / zBlockSize;
                    endZ = startZ + (1.0f / zBlockSize);
                    renderBox(
                            icon, brightness,
                            startX + pixel * 2.0f, startY, startZ + pixel * 4.0f,
                            startX + pixel * 4.0f, endY, endZ - pixel * 4.0f,
                            false
                    );
                    renderBox(
                            icon, brightness,
                            startX + pixel * 4.0f, startY, startZ + pixel * 2.0f,
                            endX - pixel * 4.0f, endY, endZ - pixel * 2.0f,
                            false
                    );
                    renderBox(
                            icon, brightness,
                            endX - pixel * 4.0f, startY, startZ + pixel * 4.0f,
                            endX - pixel * 2.0f, endY, endZ - pixel * 4.0f,
                            false
                    );
                }
            }
        }
    }

    private static void renderBox(IIcon icon, int brightness, float startX, float startY, float startZ, float endX, float endY, float endZ, boolean bottom) {
        // X faces
        renderFaceXNeg(icon, startX, startY, startZ, endX, endY, endZ);
        renderFaceXPos(icon, startX, startY, startZ, endX, endY, endZ);

        // Y faces
        if (bottom) {
            renderFaceYNeg(icon, startX, startY, startZ, endX, endY, endZ);
        }
        renderFaceYPos(icon, startX, startY, startZ, endX, endY, endZ);

        // Z faces
        renderFaceZNeg(icon, startX, startY, startZ, endX, endY, endZ);
        renderFaceZPos(icon, startX, startY, startZ, endX, endY, endZ);
    }

    private static void renderFaceXNeg(IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(0.6f, 0.6f, 0.6f);
        tessellator.addVertexWithUV(startX, endY, endZ, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(startX, endY, startZ, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(startX, startY, startZ, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(startX, startY, endZ, icon.getMaxU(), icon.getMaxV());
    }

    private static void renderFaceXPos(IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(0.6f, 0.6f, 0.6f);
        tessellator.addVertexWithUV(endX, startY, endZ, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(endX, startY, startZ, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(endX, endY, startZ, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(endX, endY, endZ, icon.getMinU(), icon.getMaxV());
    }

    private static void renderFaceYNeg(IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(0.5f, 0.5f, 0.5f);
        tessellator.addVertexWithUV(startX, startY, endZ, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(startX, startY, startZ, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(endX, startY, startZ, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(endX, startY, endZ, icon.getMaxU(), icon.getMaxV());
    }

    private static void renderFaceYPos(IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(1.0f, 1.0f, 1.0f);
        tessellator.addVertexWithUV(endX, endY, endZ, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(endX, endY, startZ, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(startX, endY, startZ, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(startX, endY, endZ, icon.getMinU(), icon.getMaxV());
    }

    private static void renderFaceZNeg(IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(0.8f, 0.8f, 0.8f);
        tessellator.addVertexWithUV(startX, endY, startZ, icon.getMaxU(), icon.getMaxV());
        tessellator.addVertexWithUV(endX, endY, startZ, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(endX, startY, startZ, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(startX, startY, startZ, icon.getMinU(), icon.getMaxV());
    }

    private static void renderFaceZPos(IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F(0.8f, 0.8f, 0.8f);
        tessellator.addVertexWithUV(startX, startY, endZ, icon.getMinU(), icon.getMaxV());
        tessellator.addVertexWithUV(endX, startY, endZ, icon.getMinU(), icon.getMinV());
        tessellator.addVertexWithUV(endX, endY, endZ, icon.getMaxU(), icon.getMinV());
        tessellator.addVertexWithUV(startX, endY, endZ, icon.getMaxU(), icon.getMaxV());
    }
}
