package billund.client.render;

import billund.init.ModBlocks;
import billund.tileentity.TileEntityBillund;
import billund.util.Stud;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;

public class BillundBlockRenderingHandler implements ISimpleBlockRenderingHandler
{
    // ISimpleBlockRenderingHandler implementation
    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return false;
    }

    @Override
    public int getRenderId()
    {
        return ModBlocks.billund.blockRenderID;
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int i, int j, int k, Block block, int modelID, RenderBlocks renderblocks)
    {
        if (modelID == getRenderId())
        {
            TileEntity entity = world.getTileEntity(i, j, k);
            if (entity != null && entity instanceof TileEntityBillund)
            {
                TileEntityBillund billund = (TileEntityBillund) entity;
                for (int x = 0; x < Stud.STUDS_PER_ROW; ++x)
                {
                    for (int y = 0; y < Stud.STUDS_PER_COLUMN; ++y)
                    {
                        for (int z = 0; z < Stud.STUDS_PER_ROW; ++z)
                        {
                            Stud stud = billund.getStudLocal(x, y, z);
                            if (stud != null)
                            {
                                int sx = i * Stud.STUDS_PER_ROW + x;
                                int sy = j * Stud.STUDS_PER_COLUMN + y;
                                int sz = k * Stud.STUDS_PER_ROW + z;
                                if (stud.XOrigin == sx && stud.YOrigin == sy && stud.ZOrigin == sz)
                                {
                                    // Draw the brick
                                    int brightness = block.getMixedBrightnessForBlock(world, i, j, k);
                                    BrickRenderHelper.renderBrick(
                                            world, brightness, stud.Colour.number,
                                            sx, sy, sz, stud.BrickWidth, stud.BrickHeight, stud.BrickDepth
                                    );
                                }
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderblocks)
    {
    }
}
