/**
 * This file is part of Billund - http://www.computercraft.info/billund
 * Copyright Daniel Ratcliffe, 2013-2014. See LICENSE for license details.
 */

package com.dan200.billund.client;

import com.dan200.billund.Billund;
import com.dan200.billund.shared.*;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class BillundProxyClient extends BillundProxyCommon
{
	public BillundProxyClient()
	{
	}
	
	// IBillundProxy implementation
	
	@Override
	public void load()
	{		
		super.load();
				
		// Setup renderers
		Billund.ModBlocks.billund.blockRenderID = RenderingRegistry.getNextAvailableRenderId();
				
		// Setup client forge handlers
		registerForgeHandlers();
	}
	
	@Override
	public boolean isClient()
	{
		return true;
	}

	@Override
	public void openOrderFormGUI( EntityPlayer player )
	{
	    GuiScreen gui = new GuiOrderForm( player );
	    FMLClientHandler.instance().displayGuiScreen( player, gui );
	}
		
	private void registerForgeHandlers()
	{
		ForgeHandlers handlers = new ForgeHandlers();
		MinecraftForge.EVENT_BUS.register(handlers);
		
		BillundBlockRenderingHandler billundHandler = new BillundBlockRenderingHandler();
		RenderingRegistry.registerBlockHandler(billundHandler);
		
		BrickRenderer brickHandler = new BrickRenderer();
		MinecraftForgeClient.registerItemRenderer(Billund.ModItems.brick, brickHandler);

		RenderingRegistry.registerEntityRenderingHandler(EntityAirDrop.class, new RenderAirDrop());
	}
				
	public class ForgeHandlers
	{
		public ForgeHandlers()
		{
		}
		
		// Forge event responses 
		
		@Mod.EventHandler
		public void onRenderWorldLast(RenderWorldLastEvent event)
		{
			// Draw the preview brick
			float f = event.partialTicks;
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			World world = Minecraft.getMinecraft().theWorld;
			if( player != null && world != null )
			{
				ItemStack currentStack = player.inventory.getCurrentItem();
				if( currentStack != null && currentStack.getItem() instanceof ItemBrick )
				{
					Brick brick = ItemBrick.getPotentialBrick( currentStack, player.worldObj, player, f );
					if( brick != null )
					{
						// Setup
						GL11.glPushMatrix();
						GL11.glBlendFunc( GL11.GL_ONE, GL11.GL_ZERO );				
						GL11.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );
						GL11.glDisable( GL11.GL_LIGHTING );
		
						translateToWorldCoords( Minecraft.getMinecraft().renderViewEntity, f );
						renderBrick( world, brick );

						// Teardown
						GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
						GL11.glEnable( GL11.GL_LIGHTING );
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
			GL11.glTranslated( -interpPosX, -interpPosY, -interpPosZ );
		}
		
		// Tick handler
    }		
		
	private static class BillundBlockRenderingHandler implements
		ISimpleBlockRenderingHandler
	{
		public BillundBlockRenderingHandler()
		{
		}
		
		// ISimpleBlockRenderingHandler implementation

		@Override
		public boolean shouldRender3DInInventory(int modelId)
		{
			return false;
		}
	
		@Override
		public int getRenderId()
		{
			return Billund.ModBlocks.billund.blockRenderID;
		}
		
		@Override
		public boolean renderWorldBlock( IBlockAccess world, int i, int j, int k, Block block, int modelID, RenderBlocks renderblocks )
		{
			if( modelID == getRenderId() )
			{
				TileEntity entity = world.getTileEntity(i, j, k);
				if( entity != null && entity instanceof TileEntityBillund )
				{
					TileEntityBillund billund = (TileEntityBillund)entity;
					for( int x=0; x<TileEntityBillund.STUDS_PER_ROW; ++x )
					{
						for( int y=0; y<TileEntityBillund.STUDS_PER_COLUMN; ++y )
						{
							for( int z=0; z<TileEntityBillund.STUDS_PER_ROW; ++z )
							{
								Stud stud = billund.getStudLocal( x, y, z );
								if( stud != null )
								{
									int sx = i*TileEntityBillund.STUDS_PER_ROW + x;
									int sy = j*TileEntityBillund.STUDS_PER_COLUMN + y;
									int sz = k*TileEntityBillund.STUDS_PER_ROW + z;
									if( stud.XOrigin == sx && stud.YOrigin == sy && stud.ZOrigin == sz )
									{
										// Draw the brick			
										int brightness = block.getMixedBrightnessForBlock( world, i, j, k );
										renderBrick(
											world, brightness, stud.Colour,
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
		public void renderInventoryBlock( Block block, int metadata, int modelID, RenderBlocks renderblocks )
		{
		}
	}
	
	public static class BrickRenderer implements
		IItemRenderer
	{
		public void BrickRenderer()
		{
		}
		
		// IItemRenderer implementation
		@Override
		public boolean handleRenderType(ItemStack item, IItemRenderer.ItemRenderType type)
		{
			switch( type )
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
		public boolean shouldUseRenderHelper( IItemRenderer.ItemRenderType type, ItemStack item, IItemRenderer.ItemRendererHelper helper )
		{
			switch( helper )
			{
				case ENTITY_ROTATION:
				case ENTITY_BOBBING:				
				case EQUIPPED_BLOCK:				
				case BLOCK_3D:
				case INVENTORY_BLOCK:				
				{
					return true;
				}
				default:
				{
					return false;
				}
			}
		}

		@Override
		public void renderItem( ItemRenderType type, ItemStack item, Object[] data )
		{
			int damage = item.getItemDamage();
			switch( type )
			{
				case ENTITY:
				{
					renderBrick( item, false, true );
					break;
				}
				case EQUIPPED:
				{
					GL11.glPushMatrix();
					GL11.glTranslatef( 0.6f, 0.6f, 0.6f );
					renderBrick( item, false, true );
					GL11.glPopMatrix();
					break;
				}
				case INVENTORY:
				{
					GL11.glPushMatrix();
					GL11.glTranslatef( 0.5f, 0.5f, 0.5f );
					renderBrick( item, true, true );
					GL11.glPopMatrix();
					break;
				}
				case EQUIPPED_FIRST_PERSON:
				default:
				{
					break;
				}
			}
		}
	}
		
	public static void renderBrick( ItemStack brick, boolean scale, boolean center )
	{
		Tessellator tessellator = Tessellator.instance;
		int brightness = 15;
				
		int colour = ItemBrick.getColour( brick );
		int width = ItemBrick.getWidth( brick );
		int height = ItemBrick.getHeight( brick );
		int depth = ItemBrick.getDepth( brick );	

		// Setup		
		GL11.glPushMatrix();

		if( scale )
		{
			float scaleValue = ((float)TileEntityBillund.LAYERS_PER_BLOCK) / Math.max( 2.0f, (float)Math.max( width, depth ) - 0.5f );
			GL11.glScalef( scaleValue, scaleValue, scaleValue );
		}
		if( center )
		{
			GL11.glTranslatef( 
				-0.5f * ((float)width / (float)TileEntityBillund.ROWS_PER_BLOCK),
				-0.5f * ((float)height / (float)TileEntityBillund.LAYERS_PER_BLOCK),
				-0.5f * ((float)depth / (float)TileEntityBillund.ROWS_PER_BLOCK)
			);		
		}

		GL11.glDisable( GL11.GL_LIGHTING );
		GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
		GL11.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );

		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture( mc.getTextureManager().getResourceLocation( 0 ) ); // bind the terrain texture
		
		tessellator.startDrawingQuads();
		tessellator.setNormal( 0.0f, -1.0f, 0.0f );
		renderBrick( null, brightness, colour, 0, 0, 0, width, height, depth );
		tessellator.draw();		
		
		// Teardown
		GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
		GL11.glEnable( GL11.GL_LIGHTING );
		GL11.glPopMatrix();
	}
	
	public static void renderBrick( IBlockAccess world, Brick brick )
	{
		int localX = (brick.XOrigin % TileEntityBillund.ROWS_PER_BLOCK + TileEntityBillund.ROWS_PER_BLOCK) % TileEntityBillund.ROWS_PER_BLOCK;
		int localY = (brick.YOrigin % TileEntityBillund.LAYERS_PER_BLOCK + TileEntityBillund.LAYERS_PER_BLOCK) % TileEntityBillund.LAYERS_PER_BLOCK;
		int localZ = (brick.ZOrigin % TileEntityBillund.ROWS_PER_BLOCK + TileEntityBillund.ROWS_PER_BLOCK) % TileEntityBillund.ROWS_PER_BLOCK;
		int blockX = (brick.XOrigin - localX) / TileEntityBillund.ROWS_PER_BLOCK;
		int blockY = (brick.YOrigin - localY) / TileEntityBillund.LAYERS_PER_BLOCK;
		int blockZ = (brick.ZOrigin - localZ) / TileEntityBillund.ROWS_PER_BLOCK;
		
		Tessellator tessellator = Tessellator.instance;
		int brightness = Billund.ModBlocks.billund.getMixedBrightnessForBlock( world, blockX, blockY, blockZ );
		
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture( mc.getTextureManager().getResourceLocation( 0 ) ); // bind the terrain texture
		
		tessellator.startDrawingQuads();
		tessellator.setNormal( 0.0f, -1.0f, 0.0f );
		renderBrick( world, brightness, brick.Colour, brick.XOrigin, brick.YOrigin, brick.ZOrigin, brick.Width, brick.Height, brick.Depth );
		tessellator.draw();
	}
	
	private static void renderBrick( IBlockAccess world, int brightness, int colour, int sx, int sy, int sz, int width, int height, int depth )
	{
		// Draw the brick
		IIcon icon = BlockBillund.getIcon( colour );
		if( world != null )
		{
			Tessellator tessellator = Tessellator.instance;
			tessellator.setBrightness( brightness );
		}
		
		float pixel = 1.0f / 96.0f;
		float xBlockSize = (float)TileEntityBillund.STUDS_PER_ROW;
		float yBlockSize = (float)TileEntityBillund.STUDS_PER_COLUMN;
		float zBlockSize = (float)TileEntityBillund.STUDS_PER_ROW;
			
		float startX = (float)sx / xBlockSize;
		float startY = (float)sy / yBlockSize;
		float startZ = (float)sz / zBlockSize;
		float endX = startX + ((float)width / xBlockSize);
		float endY = startY + ((float)height / yBlockSize);
		float endZ = startZ + ((float)depth / zBlockSize);
		renderBox(
                icon, brightness,
                startX, startY, startZ,
                endX, endY, endZ,
                true
        );

		// Draw the studs
		int sny = sy + height;
		startY = (float)sny / yBlockSize;
		endY = startY + (0.1666f / yBlockSize);
		for( int snx = sx; snx < sx + width; ++snx )
		{
			startX = (float)snx / xBlockSize;
			endX = startX + (1.0f / xBlockSize);
			for( int snz = sz; snz < sz + depth; ++snz )
			{
				boolean drawStud;
				if( world != null )
				{
					Stud above = TileEntityBillund.getStud( world, snx, sny, snz );
					drawStud = (above == null) || (above.Colour == StudColour.TranslucentWall);
				}
				else
				{
					drawStud = true;
				}
				
				if( drawStud )
				{
					startZ = (float)snz / zBlockSize;
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
	
	private static void renderBox( IIcon icon, int brightness, float startX, float startY, float startZ, float endX, float endY, float endZ, boolean bottom )
	{
		// X faces
		renderFaceXNeg( icon, startX, startY, startZ, endX, endY, endZ );
		renderFaceXPos( icon, startX, startY, startZ, endX, endY, endZ );
		
		// Y faces
		if( bottom )
		{
			renderFaceYNeg( icon, startX, startY, startZ, endX, endY, endZ );
		}
		renderFaceYPos( icon, startX, startY, startZ, endX, endY, endZ );

		// Z faces
		renderFaceZNeg( icon, startX, startY, startZ, endX, endY, endZ );
		renderFaceZPos( icon, startX, startY, startZ, endX, endY, endZ );
	}

	private static void renderFaceXNeg( IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ )
	{            
		Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F( 0.6f, 0.6f, 0.6f );
        tessellator.addVertexWithUV( startX, endY, endZ, icon.getMinU(), icon.getMaxV() );
        tessellator.addVertexWithUV( startX, endY, startZ, icon.getMinU(), icon.getMinV() );
        tessellator.addVertexWithUV( startX, startY, startZ, icon.getMaxU(), icon.getMinV() );
		tessellator.addVertexWithUV( startX, startY, endZ, icon.getMaxU(), icon.getMaxV() );
    }
    
	private static void renderFaceXPos( IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ )
	{            
		Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F( 0.6f, 0.6f, 0.6f );
		tessellator.addVertexWithUV( endX, startY, endZ, icon.getMaxU(), icon.getMaxV() );
        tessellator.addVertexWithUV( endX, startY, startZ, icon.getMaxU(), icon.getMinV() );
        tessellator.addVertexWithUV( endX, endY, startZ, icon.getMinU(), icon.getMinV() );
        tessellator.addVertexWithUV( endX, endY, endZ, icon.getMinU(), icon.getMaxV() );
    }
    
	private static void renderFaceYNeg( IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ )
	{            
		Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F( 0.5f, 0.5f, 0.5f );
        tessellator.addVertexWithUV( startX, startY, endZ, icon.getMinU(), icon.getMaxV() );
        tessellator.addVertexWithUV( startX, startY, startZ, icon.getMinU(), icon.getMinV() );
        tessellator.addVertexWithUV( endX, startY, startZ, icon.getMaxU(), icon.getMinV() );
		tessellator.addVertexWithUV( endX, startY, endZ, icon.getMaxU(), icon.getMaxV() );
    }

	private static void renderFaceYPos( IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ )
	{            
		Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F( 1.0f, 1.0f, 1.0f );
		tessellator.addVertexWithUV( endX, endY, endZ, icon.getMaxU(), icon.getMaxV() );
        tessellator.addVertexWithUV( endX, endY, startZ, icon.getMaxU(), icon.getMinV() );
        tessellator.addVertexWithUV( startX, endY, startZ, icon.getMinU(), icon.getMinV() );
        tessellator.addVertexWithUV( startX, endY, endZ, icon.getMinU(), icon.getMaxV() );
    }

	private static void renderFaceZNeg( IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ )
	{            
		Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F( 0.8f, 0.8f, 0.8f );
		tessellator.addVertexWithUV( startX, endY, startZ, icon.getMaxU(), icon.getMaxV() );
        tessellator.addVertexWithUV( endX, endY, startZ, icon.getMaxU(), icon.getMinV() );
        tessellator.addVertexWithUV( endX, startY, startZ, icon.getMinU(), icon.getMinV() );
        tessellator.addVertexWithUV( startX, startY, startZ, icon.getMinU(), icon.getMaxV() );
    }

	private static void renderFaceZPos( IIcon icon, float startX, float startY, float startZ, float endX, float endY, float endZ )
	{            
		Tessellator tessellator = Tessellator.instance;
        tessellator.setColorOpaque_F( 0.8f, 0.8f, 0.8f );
        tessellator.addVertexWithUV( startX, startY, endZ, icon.getMinU(), icon.getMaxV() );
        tessellator.addVertexWithUV( endX, startY, endZ, icon.getMinU(), icon.getMinV() );
        tessellator.addVertexWithUV( endX, endY, endZ, icon.getMaxU(), icon.getMinV() );
		tessellator.addVertexWithUV( startX, endY, endZ, icon.getMaxU(), icon.getMaxV() );
    }
}
